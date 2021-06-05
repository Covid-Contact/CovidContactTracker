package cat.covidcontact.usecases.sendmessagingtoken

import cat.covidcontact.usecases.UseCase

interface SendMessagingToken : UseCase<SendMessagingToken.Request, SendMessagingToken.Response> {

    data class Request(
        val email: String
    ) : UseCase.UseCaseRequest

    class Response : UseCase.UseCaseResponse
}
