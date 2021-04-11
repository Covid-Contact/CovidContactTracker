package cat.covidcontact.data.repositories.user

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.user.UserController
import cat.covidcontact.model.ApplicationUser
import cat.covidcontact.model.Device
import cat.covidcontact.model.post.PostUser
import cat.covidcontact.model.user.User
import com.google.gson.Gson

class UserRepositoryImpl(
    private val userController: UserController
) : UserRepository {

    override suspend fun makeLogIn(email: String, password: String) {
        val serverResponse = userController.isUserValidated(email)
        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@run
                HttpStatus.NOT_FOUND -> throw UserException.EmailNotFoundException(email)
                HttpStatus.BAD_REQUEST -> throw UserException.EmailNotValidatedException(email)
                else -> throw CommonException.OtherError
            }
        }

        if (!serverResponse.result.get().toBoolean()) {
            throw UserException.EmailNotValidatedException(email)
        }

        makeLogInRequest(email, password)
    }

    private suspend fun makeLogInRequest(email: String, password: String) {
        val serverResponse = userController.makeLogIn(ApplicationUser(email, password))
        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@run
                HttpStatus.FORBIDDEN -> throw UserException.WrongPasswordException
                HttpStatus.NOT_FOUND -> throw UserException.EmailNotFoundException(email)
                else -> throw CommonException.OtherError
            }
        }

        val token = serverResponse.response[CovidContactBaseController.AUTH_HEADER].first()
            .toString()
        CovidContactBaseController.token = token
    }

    override suspend fun makeSignUp(email: String, password: String) {
        val serverResponse = userController.makeSignUp(ApplicationUser(email, password))
        when (serverResponse.response.statusCode) {
            CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
            HttpStatus.CREATED -> return
            HttpStatus.BAD_REQUEST -> throw UserException.EmailAlreadyRegistered(email)
            else -> throw CommonException.OtherError
        }
    }

    override suspend fun getUserData(email: String): User {
        val serverResponse = userController.getUserData(email)
        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@run
                HttpStatus.NOT_FOUND -> throw UserException.UserInfoNotFound(email)
                else -> throw CommonException.OtherError
            }
        }

        val postUser = Gson().fromJson(serverResponse.result.get(), PostUser::class.java)
        return User.fromPost(postUser)
    }

    override suspend fun addUserData(user: User): String {
        val serverResponse = userController.addUserData(user.createPost())
        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@run
                HttpStatus.BAD_REQUEST -> UserException.UserInfoFound(user.email)
                else -> throw CommonException.OtherError
            }
        }

        return user.email
    }

    override suspend fun registerUserDevice(email: String, device: Device) {
        val serverResponse = userController.registerUserDevice(email, device.createPost())
        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@run
                HttpStatus.NOT_FOUND -> throw UserException.EmailNotFoundException(email)
                else -> throw CommonException.OtherError
            }
        }
    }
}
