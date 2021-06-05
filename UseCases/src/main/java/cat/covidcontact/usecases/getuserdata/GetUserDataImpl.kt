package cat.covidcontact.usecases.getuserdata

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class GetUserDataImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val contactNetworkRepository: ContactNetworkRepository
) : GetUserData {
    override suspend fun execute(request: GetUserData.Request) = runUseCase {
        val user = userRepository.getUserData(request.email)
        val contactNetworks = contactNetworkRepository.getContactNetworks(request.email)

        user.addContactNetworks(contactNetworks)
        GetUserData.Response(user)
    }
}
