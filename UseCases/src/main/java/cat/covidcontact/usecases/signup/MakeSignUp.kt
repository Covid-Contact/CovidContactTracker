package cat.covidcontact.usecases.signup

import cat.covidcontact.usecases.UseCase

interface MakeSignUp : UseCase<MakeSignUp.Request, MakeSignUp.Response> {
    data class Request(
        val email: String,
        val password: String
    ) : UseCase.UseCaseRequest

    data class Response(
        val email: String
    ) : UseCase.UseCaseResponse
}