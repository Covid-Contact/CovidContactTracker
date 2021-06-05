package cat.covidcontact.usecases.getContactNetworkByAccessCode

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class GetContactNetworkByAccessCodeImpl @Inject constructor(
    private val contactNetworkRepository: ContactNetworkRepository
) : GetContactNetworkByAccessCode {

    override suspend fun execute(request: GetContactNetworkByAccessCode.Request) = runUseCase {
        val contactNetwork = contactNetworkRepository.getContactNetworkByAccessCode(
            request.accessCode
        )
        GetContactNetworkByAccessCode.Response(contactNetwork)
    }
}
