package cat.covidcontact.usecases.adduserdata

import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface AddUserData : UseCase<AddUserData.Request, AddUserData.Response> {

    data class Request(
        val user: User
    ) : UseCase.UseCaseRequest

    data class Response(
        val email: String
    ) : UseCase.UseCaseResponse
}