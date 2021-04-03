package cat.covidcontact.data.user

import android.util.Log
import cat.covidcontact.data.CommonException
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.UserController
import cat.covidcontact.model.ApplicationUser
import cat.covidcontact.model.User

class UserRepositoryImpl(
    private val userController: UserController
) : UserRepository {

    override suspend fun makeLogIn(email: String, password: String) {
        val serverResponse = userController.isUserValidated(email)
        when (serverResponse.response.statusCode) {
            CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
            HttpStatus.OK -> {
                if (!serverResponse.result.get().toBoolean()) {
                    throw UserException.EmailNotValidatedException(email)
                }

                makeLogInRequest(email, password)
            }
            HttpStatus.NOT_FOUND -> throw UserException.EmailNotFoundException(email)
            HttpStatus.BAD_REQUEST -> throw UserException.EmailNotValidatedException(email)
            else -> throw CommonException.OtherError
        }
    }

    private suspend fun makeLogInRequest(email: String, password: String) {
        val serverResponse = userController.makeLogIn(ApplicationUser(email, password))
        Log.i("Test", "makeLogInRequest: ${serverResponse.response}")
        when (serverResponse.response.statusCode) {
            CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
            HttpStatus.NOT_FOUND -> throw UserException.EmailNotFoundException(email)
            HttpStatus.SERVER_ERROR -> throw CommonException.OtherError
        }

        val token = serverResponse.response["Authorization"].toString()
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
        TODO("Not yet implemented")
    }
}
