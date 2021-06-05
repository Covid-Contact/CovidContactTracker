package cat.covidcontact.usecases.deletecontactnetwork

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class DeleteContactNetworkImpl @Inject constructor(
    private val contactNetworkRepository: ContactNetworkRepository
) : DeleteContactNetwork {

    override suspend fun execute(request: DeleteContactNetwork.Request) = runUseCase {
        contactNetworkRepository.deleteContactNetwork(
            request.contactNetwork.name,
            request.user.email
        )
        DeleteContactNetwork.Response(request.contactNetwork)
    }
}
