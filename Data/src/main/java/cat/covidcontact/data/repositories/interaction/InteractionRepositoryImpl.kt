package cat.covidcontact.data.repositories.interaction

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.interaction.InteractionController
import cat.covidcontact.model.post.PostRead
import javax.inject.Inject

class InteractionRepositoryImpl @Inject constructor(
    private val interactionController: InteractionController
) : InteractionRepository {

    override suspend fun registerRead(currentDeviceId: String, deviceIds: Set<String>, time: Long) {
        val serverResponse = interactionController.registerRead(
            PostRead(
                currentDeviceId = currentDeviceId,
                deviceIds = deviceIds.toList(),
                dateTime = time
            )
        )

        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@run
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun notifyPositive(email: String) {
        val serverResponse = interactionController.notifyPositive(email)
        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@run
                else -> throw CommonException.OtherError
            }
        }
    }
}
