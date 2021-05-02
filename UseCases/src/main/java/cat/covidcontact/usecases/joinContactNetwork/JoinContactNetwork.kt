package cat.covidcontact.usecases.joinContactNetwork

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface JoinContactNetwork : UseCase<JoinContactNetwork.Request, JoinContactNetwork.Response> {

    data class Request(
        val user: User,
        val contactNetwork: ContactNetwork
    ) : UseCase.UseCaseRequest

    class Response : UseCase.UseCaseResponse
}
