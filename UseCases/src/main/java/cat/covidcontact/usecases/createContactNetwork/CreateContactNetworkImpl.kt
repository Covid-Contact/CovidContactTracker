package cat.covidcontact.usecases.createContactNetwork

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class CreateContactNetworkImpl @Inject constructor(
    private val contactNetworkRepository: ContactNetworkRepository
) : CreateContactNetwork {

    override suspend fun execute(request: CreateContactNetwork.Request) = runUseCase {
        val contactNetwork = with(request) {
            contactNetworkRepository.createContactNetwork(name, password, owner)
        }

        request.owner.addContactNetwork(contactNetwork)
        CreateContactNetwork.Response(contactNetwork)
    }
}
