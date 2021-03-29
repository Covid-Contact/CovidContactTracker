package cat.covidcontact.data.controllers

import cat.covidcontact.model.ApplicationUser

class UserControllerImpl : UserController() {

    override suspend fun makeSignUp(applicationUser: ApplicationUser): ServerResponse {
        return post("$url/signup", applicationUser, isAuthenticated = false)
    }
}