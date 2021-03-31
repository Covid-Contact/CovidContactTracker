package cat.covidcontact.data.controllers

import cat.covidcontact.model.ApplicationUser

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
}