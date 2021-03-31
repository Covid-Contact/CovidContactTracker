package cat.covidcontact.data.user

interface UserRepository {
    suspend fun makeLogIn(email: String, password: String)

    suspend fun makeSignUp(email: String, password: String)
}