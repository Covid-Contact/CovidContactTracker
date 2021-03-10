package cat.covidcontact.tracker.usecase.signup

import cat.covidcontact.tracker.usecase.UseCase

interface MakeSignUp : UseCase<MakeSignUp.Request, MakeSignUp.Response> {
    data class Request(
        val email: String,
        val password: String
    ) : UseCase.UseCaseRequest

    data class Response(
        val email: String
    ) : UseCase.UseCaseResponse
}