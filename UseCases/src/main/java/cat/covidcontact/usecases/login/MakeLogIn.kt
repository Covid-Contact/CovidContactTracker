package cat.covidcontact.usecases.login

import cat.covidcontact.usecases.UseCase

interface MakeLogIn : UseCase<MakeLogIn.Request, MakeLogIn.Response> {
    data class Request(
        val email: String,
        val password: String
    ) : UseCase.UseCaseRequest

    data class Response(
        val email: String
    ) : UseCase.UseCaseResponse
}
