package cat.covidcontact.usecases.registerDevice

import cat.covidcontact.model.Device
import cat.covidcontact.model.UserDevice
import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.UseCase

interface RegisterDevice : UseCase<RegisterDevice.Request, RegisterDevice.Response> {

    data class Request(
        val user: User,
        val device: Device
    ) : UseCase.UseCaseRequest

    data class Response(
        val userDevice: UserDevice
    ) : UseCase.UseCaseResponse
}
