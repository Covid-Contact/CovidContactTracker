package cat.covidcontact.usecases.login

import cat.covidcontact.data.user.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class MakeLogInImpl @Inject constructor(
    private val userRepository: UserRepository
) : MakeLogIn {

    override suspend fun execute(request: MakeLogIn.Request) = runUseCase {
        userRepository.makeLogIn(request.email, request.password)
        MakeLogIn.Response(request.email)
    }
}
