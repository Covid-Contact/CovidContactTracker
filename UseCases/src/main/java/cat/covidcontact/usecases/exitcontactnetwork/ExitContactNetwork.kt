package cat.covidcontact.usecases.exitcontactnetwork

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface ExitContactNetwork : UseCase<ExitContactNetwork.Request, ExitContactNetwork.Response> {

    data class Request(
        val user: User,
        val contactNetwork: ContactNetwork
    ) : UseCase.UseCaseRequest

    class Response : UseCase.UseCaseResponse
}
