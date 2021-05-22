package cat.covidcontact.usecases.sendread

import cat.covidcontact.usecases.UseCase

interface SendRead : UseCase<SendRead.Request, SendRead.Response> {

    data class Request(
        val currentDeviceId: String,
        val deviceIds: Set<String>,
        val time: Long
    ) : UseCase.UseCaseRequest

    data class Response(
        val isEnded: Boolean
    ) : UseCase.UseCaseResponse
}
