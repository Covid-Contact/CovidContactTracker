package cat.covidcontact.usecases.getuserdata

import cat.covidcontact.data.user.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class GetUserDataImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserData {
    override suspend fun execute(request: GetUserData.Request) = runUseCase {
        val userInfo = userRepository.getUserData(request.email)
        GetUserData.Result(userInfo)
    }
}
