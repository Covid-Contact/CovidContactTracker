package cat.covidcontact.usecases.adduserdata

import cat.covidcontact.data.user.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class AddUserDataImpl @Inject constructor(
    private val userRepository: UserRepository
) : AddUserData {
    override suspend fun execute(request: AddUserData.Request) = runUseCase {
        val result = userRepository.addUserData(request.user)
        AddUserData.Response(result)
    }
}
