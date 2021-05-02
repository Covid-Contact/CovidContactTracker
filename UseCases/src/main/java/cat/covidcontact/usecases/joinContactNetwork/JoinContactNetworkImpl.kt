package cat.covidcontact.usecases.joinContactNetwork

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class JoinContactNetworkImpl @Inject constructor(
    private val contactNetworkRepository: ContactNetworkRepository
) : JoinContactNetwork {
    override suspend fun execute(request: JoinContactNetwork.Request) = runUseCase {
        val user = request.user
        val contactNetwork = request.contactNetwork

        contactNetworkRepository.joinContactNetwork(user.email, contactNetwork.name)
        user.addContactNetwork(contactNetwork)

        JoinContactNetwork.Response()
    }
}
