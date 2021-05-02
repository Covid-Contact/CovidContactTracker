package cat.covidcontact.usecases.getContactNetworkByAccessCode

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.usecases.UseCase

interface GetContactNetworkByAccessCode :
    UseCase<GetContactNetworkByAccessCode.Request, GetContactNetworkByAccessCode.Response> {

    data class Request(
        val accessCode: String
    ) : UseCase.UseCaseRequest

    data class Response(
        val contactNetwork: ContactNetwork
    ) : UseCase.UseCaseResponse
}
