package cat.covidcontact.usecases.deletecontactnetwork

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface DeleteContactNetwork :
    UseCase<DeleteContactNetwork.Request, DeleteContactNetwork.Response> {

    data class Request(
        val contactNetwork: ContactNetwork,
        val user: User
    ) : UseCase.UseCaseRequest

    data class Response(
        val contactNetwork: ContactNetwork
    ) : UseCase.UseCaseResponse
}
