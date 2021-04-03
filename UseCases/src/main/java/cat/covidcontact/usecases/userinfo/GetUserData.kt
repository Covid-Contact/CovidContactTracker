package cat.covidcontact.usecases.userinfo

import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface GetUserData : UseCase<GetUserData.Request, GetUserData.Result> {
    data class Request(
        val email: String
    ) : UseCase.UseCaseRequest

    data class Result(
        val user: User
    ) : UseCase.UseCaseResponse
}
