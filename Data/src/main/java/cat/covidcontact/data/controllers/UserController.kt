package cat.covidcontact.data.controllers

import cat.covidcontact.model.ApplicationUser

abstract class UserController : CovidContactBaseController() {
    override val url: String
        get() = super.url + "/user"

    abstract suspend fun makeSignUp(applicationUser: ApplicationUser): ServerResponse
    abstract suspend fun isUserValidated(email: String): ServerResponse
    abstract suspend fun makeLogIn(applicationUser: ApplicationUser): ServerResponse
}