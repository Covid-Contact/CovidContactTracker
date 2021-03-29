package cat.covidcontact.usecases.signup

import cat.covidcontact.data.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class MakeSignUpImpl @Inject constructor(
    private val userRepository: UserRepository
) : MakeSignUp {
    override suspend fun execute(request: MakeSignUp.Request) = runUseCase {
        userRepository.makeSignUp(request.email, request.password)
        MakeSignUp.Response(request.email)
    }
}
