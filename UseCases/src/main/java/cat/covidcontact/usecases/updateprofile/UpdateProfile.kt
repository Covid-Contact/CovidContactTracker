package cat.covidcontact.usecases.updateprofile

import cat.covidcontact.usecases.UseCase

interface UpdateProfile : UseCase<UpdateProfile.Request, UpdateProfile.Response> {

    data class Request(
        val email: String,
        val city: String,
        val studies: String,
        val occupation: String,
        val marriage: String,
        val children: Int,
        val positive: Boolean?,
        val vaccinated: Boolean?
    ) : UseCase.UseCaseRequest

    class Response : UseCase.UseCaseResponse
}
