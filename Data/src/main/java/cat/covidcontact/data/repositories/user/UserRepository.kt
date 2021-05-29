package cat.covidcontact.data.repositories.user

import cat.covidcontact.model.Device
import cat.covidcontact.model.user.User

interface UserRepository {
    suspend fun makeLogIn(email: String, password: String)
    suspend fun makeSignUp(email: String, password: String)
    suspend fun getUserData(email: String): User
    suspend fun addUserData(user: User): String
    suspend fun registerUserDevice(email: String, device: Device)
    suspend fun sendMessagingToken(email: String)

    suspend fun updateUserData(
        email: String,
        city: String,
        studies: String,
        occupation: String,
        marriage: String,
        children: Int,
        positive: Boolean?,
        vaccinated: Boolean?
    )
}
