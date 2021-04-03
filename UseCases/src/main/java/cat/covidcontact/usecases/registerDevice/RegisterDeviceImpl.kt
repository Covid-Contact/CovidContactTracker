package cat.covidcontact.usecases.registerDevice

import cat.covidcontact.data.user.UserRepository
import cat.covidcontact.model.UserDevice
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class RegisterDeviceImpl @Inject constructor(
    private val userRepository: UserRepository
) : RegisterDevice {
    override suspend fun execute(request: RegisterDevice.Request) = runUseCase {
        userRepository.registerUserDevice(request.user.email, request.device)
        RegisterDevice.Response(UserDevice(request.user, request.device))
    }
}
