package cat.covidcontact.data.controllers

import cat.covidcontact.model.ApplicationUser

abstract class UserController : CovidContactBaseController() {
    override val url: String
        get() = super.url + "/user"

    abstract suspend fun makeSignUp(applicationUser: ApplicationUser): ServerResponse
}