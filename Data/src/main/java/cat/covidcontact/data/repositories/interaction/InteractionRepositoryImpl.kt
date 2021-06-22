/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

    override suspend fun registerRead(
        currentDeviceId: String,
        deviceIds: Set<String>,
        time: Long,
        lat: Double?,
        lon: Double?
    ) {
        val serverResponse = interactionController.registerRead(
            PostRead(
                currentDeviceId = currentDeviceId,
                deviceIds = deviceIds.toList(),
                dateTime = time,
                lat = lat,
                lon = lon
            )
        )

        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun notifyPositive(email: String) {
        val serverResponse = interactionController.notifyPositive(email)
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }
    }
}
