package cat.covidcontact.usecases.generateAccessCode

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class GenerateAccessCodeImpl @Inject constructor(
    private val contactNetworkRepository: ContactNetworkRepository
) : GenerateAccessCode {
    override suspend fun execute(request: GenerateAccessCode.Request) = runUseCase {
        val accessCode = contactNetworkRepository.generateAccessCode(
            request.email,
            request.contactNetworkName
        )

        GenerateAccessCode.Response(accessCode)
    }
}
