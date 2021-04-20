package cat.covidcontact.usecases.enableUserAddition

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class EnableUserAdditionImpl @Inject constructor(
    private val contactNetworkRepository: ContactNetworkRepository
) : EnableUserAddition {
    override suspend fun execute(request: EnableUserAddition.Request) = runUseCase {
        contactNetworkRepository.enableUserAddition(request.contactNetwork.name, request.isEnabled)
        request.contactNetwork.isVisible = request.isEnabled
        EnableUserAddition.Response()
    }
}
