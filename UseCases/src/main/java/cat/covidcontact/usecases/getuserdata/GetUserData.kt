package cat.covidcontact.usecases.getuserdata

import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface GetUserData : UseCase<GetUserData.Request, GetUserData.Response> {
    data class Request(
        val email: String
    ) : UseCase.UseCaseRequest

    data class Response(
        val user: User
    ) : UseCase.UseCaseResponse
}
