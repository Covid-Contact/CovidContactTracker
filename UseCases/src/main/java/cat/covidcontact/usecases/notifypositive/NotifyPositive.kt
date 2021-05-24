package cat.covidcontact.usecases.notifypositive

import cat.covidcontact.usecases.UseCase

interface NotifyPositive : UseCase<NotifyPositive.Request, NotifyPositive.Response> {

    data class Request(
        val email: String
    ) : UseCase.UseCaseRequest

    class Response : UseCase.UseCaseResponse
}
