package cat.covidcontact.usecases.makelogout

import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class MakeLogOutImpl @Inject constructor(
    private val userRepository: UserRepository
) : MakeLogOut {
    override suspend fun execute(request: MakeLogOut.Request) = runUseCase {
        userRepository.makeLogOut(request.email, request.device)
        MakeLogOut.Response()
    }
}
