package cat.covidcontact.usecases.signup

import android.util.Log
import cat.covidcontact.data.UserRepository
import cat.covidcontact.usecases.UseCaseResult
import javax.inject.Inject

class MakeSignUpImpl @Inject constructor(
    private val userRepository: UserRepository
) : MakeSignUp {
    override suspend fun execute(request: MakeSignUp.Request): UseCaseResult<MakeSignUp.Response> =
        try {
            Log.i("Test", "execute: HERE")
            userRepository.makeSignUp(request.email, request.password)
            UseCaseResult.Success(MakeSignUp.Response(request.email))
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
}
