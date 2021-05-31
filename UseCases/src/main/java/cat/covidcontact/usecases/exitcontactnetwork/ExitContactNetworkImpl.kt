package cat.covidcontact.usecases.exitcontactnetwork

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase

class ExitContactNetworkImpl(
    private val contactNetworkRepository: ContactNetworkRepository
) : ExitContactNetwork {

    override suspend fun execute(request: ExitContactNetwork.Request) = runUseCase {
        contactNetworkRepository.exitContactNetwork(
            email = request.user.email,
            contactNetworkName = request.contactNetwork.name
        )

        request.user.removeContactNetwork(request.contactNetwork)
        ExitContactNetwork.Response(request.user)
    }
}
