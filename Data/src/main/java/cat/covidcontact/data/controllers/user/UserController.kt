package cat.covidcontact.data.controllers.user

import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.ApplicationUser
import cat.covidcontact.model.post.PostDevice
import cat.covidcontact.model.post.PostUser

abstract class UserController : CovidContactBaseController() {
    override val url = "${super.url}/user"

    abstract suspend fun makeSignUp(applicationUser: ApplicationUser): ServerResponse
    abstract suspend fun isUserValidated(email: String): ServerResponse
    abstract suspend fun makeLogIn(applicationUser: ApplicationUser): ServerResponse
    abstract suspend fun getUserData(email: String): ServerResponse
    abstract suspend fun addUserData(user: PostUser): ServerResponse
    abstract suspend fun registerUserDevice(email: String, device: PostDevice): ServerResponse
}