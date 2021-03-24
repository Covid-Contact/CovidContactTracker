package cat.covidcontact.data.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.Serializable

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("/user/signup")
    suspend fun makeSignUp(@Body body: EmailPassword): Call<Unit>

    /*suspend fun makeLogIn(@Body body: EmailPassword)

    suspend fun isUserValidated(email: String): Boolean*/

    data class EmailPassword(
        val email: String,
        val password: String
    ) : Serializable
}