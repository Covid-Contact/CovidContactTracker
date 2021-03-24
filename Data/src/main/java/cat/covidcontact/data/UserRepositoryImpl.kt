package cat.covidcontact.data

import android.util.Log
import cat.covidcontact.data.services.HttpRequest
import cat.covidcontact.data.services.Server
import cat.covidcontact.data.services.UserService

class UserRepositoryImpl(
    private val httpRequest: HttpRequest
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
        val result = httpRequest.make(
            Server.userService.makeSignUp(
                UserService.EmailPassword(email, password)
            )
        )

        if (result.isSuccessful) {
            Log.i("Test", "makeSignUp: CORRECT")
        } else {
            Log.i("Test", "makeSignUp: ERROR")
            Log.i("Test", "makeSignUp: ${result.failure}")
        }
    }
}