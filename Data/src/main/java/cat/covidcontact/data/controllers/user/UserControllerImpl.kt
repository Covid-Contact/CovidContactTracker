package cat.covidcontact.data.controllers.user

import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.ApplicationUser
import cat.covidcontact.model.post.PostDevice
import cat.covidcontact.model.post.PostToken
import cat.covidcontact.model.post.PostUser

class UserControllerImpl : UserController() {

    override suspend fun makeSignUp(applicationUser: ApplicationUser): ServerResponse {
        return post("$url/signup", applicationUser, isAuthenticated = false)
    }

    override suspend fun isUserValidated(email: String): ServerResponse {
        return get("$url/validated", listOf("email" to email), isAuthenticated = false)
    }

    override suspend fun makeLogIn(applicationUser: ApplicationUser): ServerResponse {
        return post("$url/login", applicationUser, isAuthenticated = false)
    }

    override suspend fun getUserData(email: String): ServerResponse {
        return get("$url/userinfo", listOf("email" to email))
    }

    override suspend fun addUserData(user: PostUser): ServerResponse {
        return post("$url/userinfo", user)
    }

    override suspend fun registerUserDevice(email: String, device: PostDevice): ServerResponse {
        return post("$url/device", device, listOf("email" to email))
    }

    override suspend fun sendMessagingToken(token: PostToken): ServerResponse {
        return post("$url/messagetoken", token)
    }

    override suspend fun updateUserProfile(
        email: String,
        city: String,
        studies: String,
        occupation: String,
        marriage: String,
        children: Int,
        positive: Boolean?,
        vaccinated: Boolean?
    ): ServerResponse {
        return put(
            "$url/update",
            listOf(
                "email" to email,
                "city" to city,
                "studies" to studies,
                "occupation" to occupation,
                "marriage" to marriage,
                "children" to children,
                "positive" to positive,
                "vaccinated" to vaccinated
            )
        )
    }

    override suspend fun makeLogOut(email: String, deviceId: String): ServerResponse {
        return put("$url/logout", listOf("email" to email, "deviceId" to deviceId))
    }

    override suspend fun deleteAccount(email: String): ServerResponse {
        return delete("$url/delete", listOf("email" to email))
    }
}
