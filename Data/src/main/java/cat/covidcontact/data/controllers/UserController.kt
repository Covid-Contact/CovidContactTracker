package cat.covidcontact.data.controllers

import cat.covidcontact.model.ApplicationUser
import cat.covidcontact.model.Device
import cat.covidcontact.model.user.User

abstract class UserController : CovidContactBaseController() {
    override val url: String
        get() = super.url + "/user"

    abstract suspend fun makeSignUp(applicationUser: ApplicationUser): ServerResponse
    abstract suspend fun isUserValidated(email: String): ServerResponse
    abstract suspend fun makeLogIn(applicationUser: ApplicationUser): ServerResponse
    abstract suspend fun getUserData(email: String): ServerResponse
    abstract suspend fun addUserData(user: User): ServerResponse
    abstract suspend fun registerUserDevice(email: String, device: Device): ServerResponse
}