package cat.covidcontact.usecases.generateAccessCode

import cat.covidcontact.usecases.UseCase

interface GenerateAccessCode : UseCase<GenerateAccessCode.Request, GenerateAccessCode.Response> {

    data class Request(
        val email: String,
        val contactNetworkName: String
    ) : UseCase.UseCaseRequest

    data class Response(
        val accessCode: String
    ) : UseCase.UseCaseResponse
}
