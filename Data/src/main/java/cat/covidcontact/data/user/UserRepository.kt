package cat.covidcontact.data.user

import cat.covidcontact.model.user.User

interface UserRepository {
    suspend fun makeLogIn(email: String, password: String)

    suspend fun makeSignUp(email: String, password: String)

    suspend fun getUserData(email: String): User

    suspend fun addUserData(user: User): String
}