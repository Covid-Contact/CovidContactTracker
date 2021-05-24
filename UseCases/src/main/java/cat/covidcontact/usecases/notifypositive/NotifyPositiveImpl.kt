package cat.covidcontact.usecases.notifypositive

import cat.covidcontact.data.repositories.interaction.InteractionRepository
import cat.covidcontact.model.NetworkState
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class NotifyPositiveImpl @Inject constructor(
    private val interactionRepository: InteractionRepository
) : NotifyPositive {

    override suspend fun execute(request: NotifyPositive.Request) = runUseCase {
        interactionRepository.notifyPositive(request.user.email)
        request.user.contactNetworks.forEach { contactNetwork ->
            contactNetwork.networkState = NetworkState.PositiveDetected
        }
        NotifyPositive.Response(request.user)
    }
}
