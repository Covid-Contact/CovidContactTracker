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
