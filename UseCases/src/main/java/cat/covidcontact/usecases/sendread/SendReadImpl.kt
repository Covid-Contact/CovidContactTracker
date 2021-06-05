package cat.covidcontact.usecases.sendread

import cat.covidcontact.data.repositories.interaction.InteractionRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class SendReadImpl @Inject constructor(
    private val interactionRepository: InteractionRepository
) : SendRead {

    override suspend fun execute(request: SendRead.Request) = runUseCase {
        interactionRepository.registerRead(
            request.currentDeviceId,
            request.deviceIds,
            request.time,
            request.lat,
            request.lon
        )
        SendRead.Response(request.deviceIds.isEmpty())
    }
}
