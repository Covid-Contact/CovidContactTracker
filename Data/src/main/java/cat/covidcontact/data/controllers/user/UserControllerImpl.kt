package cat.covidcontact.data.controllers.user

import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.ApplicationUser
import cat.covidcontact.model.post.PostDevice
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
}
