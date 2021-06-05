package cat.covidcontact.usecases.makelogout

import cat.covidcontact.usecases.UseCase

interface MakeLogOut : UseCase<MakeLogOut.Request, MakeLogOut.Response> {

    data class Request(
        val email: String,
        val device: String
    ) : UseCase.UseCaseRequest

    class Response : UseCase.UseCaseResponse
}
