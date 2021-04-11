package cat.covidcontact.usecases.createContactNetwork

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface CreateContactNetwork :
    UseCase<CreateContactNetwork.Request, CreateContactNetwork.Response> {

    data class Request(
        val name: String,
        val password: String?,
        val owner: User
    ) : UseCase.UseCaseRequest

    data class Response(
        val contactNetwork: ContactNetwork
    ) : UseCase.UseCaseResponse
}
