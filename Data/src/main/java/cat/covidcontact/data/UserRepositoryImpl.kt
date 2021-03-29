package cat.covidcontact.data

import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.UserController
import cat.covidcontact.model.ApplicationUser

class UserRepositoryImpl(
    private val userController: UserController
) : UserRepository {

    override suspend fun makeLogIn(email: String, password: String) {
        /*if (!userService.isUserValidated(email)) {
            throw UserException.EmailNotValidatedException(email)
        }

        try {
            userService.makeLogIn(email, password)
        } catch (e: Exception) {

        }*/
    }

    override suspend fun makeSignUp(email: String, password: String) {
        val serverResponse = userController.makeSignUp(ApplicationUser(email, password))
        when (serverResponse.response.statusCode) {
            CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
            HttpStatus.BAD_REQUEST -> throw UserException.EmailAlreadyRegistered(email)
            else -> CommonException.OtherError
        }
    }
}
