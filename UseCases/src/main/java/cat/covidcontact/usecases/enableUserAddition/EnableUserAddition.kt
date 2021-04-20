package cat.covidcontact.usecases.enableUserAddition

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.usecases.UseCase

interface EnableUserAddition : UseCase<EnableUserAddition.Request, EnableUserAddition.Response> {

    data class Request(
        val contactNetwork: ContactNetwork,
        val isEnabled: Boolean
    ) : UseCase.UseCaseRequest

    class Response : UseCase.UseCaseResponse
}
