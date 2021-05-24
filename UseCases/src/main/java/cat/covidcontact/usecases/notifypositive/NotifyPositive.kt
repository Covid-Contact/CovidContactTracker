package cat.covidcontact.usecases.notifypositive

import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface NotifyPositive : UseCase<NotifyPositive.Request, NotifyPositive.Response> {

    data class Request(
        val user: User
    ) : UseCase.UseCaseRequest

    data class Response(
        val user: User
    ) : UseCase.UseCaseResponse
}
