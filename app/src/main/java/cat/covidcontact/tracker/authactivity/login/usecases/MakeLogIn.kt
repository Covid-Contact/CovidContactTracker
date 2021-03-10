package cat.covidcontact.tracker.authactivity.login.usecases

import cat.covidcontact.tracker.authactivity.model.User
import cat.covidcontact.tracker.usecase.UseCase

interface MakeLogIn : UseCase<MakeLogIn.Request, MakeLogIn.Response> {
    data class Request(
        val email: String,
        val password: String
    ) : UseCase.UseCaseRequest

    data class Response(
        val user: User
    ) : UseCase.UseCaseResponse
}