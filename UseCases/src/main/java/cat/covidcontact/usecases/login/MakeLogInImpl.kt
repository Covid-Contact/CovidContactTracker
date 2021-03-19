package cat.covidcontact.usecases.login

import cat.covidcontact.data.UserRepository
import cat.covidcontact.usecases.UseCaseResult
import javax.inject.Inject

class MakeLogInImpl @Inject constructor(
    private val userRepository: UserRepository
) : MakeLogIn {
    override fun execute(request: MakeLogIn.Request): UseCaseResult<MakeLogIn.Response> = try {
        userRepository.makeLogIn(request.email, request.password)
        UseCaseResult.Success(MakeLogIn.Response(request.email))
    } catch (e: Exception) {
        UseCaseResult.Error(e)
    }
}