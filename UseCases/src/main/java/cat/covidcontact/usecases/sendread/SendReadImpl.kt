package cat.covidcontact.usecases.sendread

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class SendReadImpl @Inject constructor(
    private val contactNetworkRepository: ContactNetworkRepository
) : SendRead {

    override suspend fun execute(request: SendRead.Request) = runUseCase {
        contactNetworkRepository.registerRead(request.deviceIds, request.time)
        SendRead.Response()
    }
}
