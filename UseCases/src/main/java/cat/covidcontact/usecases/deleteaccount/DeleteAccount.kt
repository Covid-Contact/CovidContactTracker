package cat.covidcontact.usecases.deleteaccount

import cat.covidcontact.usecases.UseCase

interface DeleteAccount : UseCase<DeleteAccount.Request, DeleteAccount.Response> {

    data class Request(
        val email: String
    ) : UseCase.UseCaseRequest

    class Response : UseCase.UseCaseResponse
}
