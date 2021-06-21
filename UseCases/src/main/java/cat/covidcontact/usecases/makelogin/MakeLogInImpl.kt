package cat.covidcontact.usecases.makelogin

import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class MakeLogInImpl @Inject constructor(
    private val userRepository: UserRepository
) : MakeLogIn {

    override suspend fun execute(request: MakeLogIn.Request) = runUseCase {
        userRepository.validateAndMakeLogIn(request.email, request.password)
        MakeLogIn.Response(request.email)
    }
}
