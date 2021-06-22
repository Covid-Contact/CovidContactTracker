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

package cat.covidcontact.usecases.deletecontactnetwork

import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.runUseCase
import javax.inject.Inject

class DeleteContactNetworkImpl @Inject constructor(
    private val contactNetworkRepository: ContactNetworkRepository
) : DeleteContactNetwork {

    override suspend fun execute(request: DeleteContactNetwork.Request) = runUseCase {
        contactNetworkRepository.deleteContactNetwork(
            request.contactNetwork.name,
            request.user.email
        )
        DeleteContactNetwork.Response(request.contactNetwork)
    }
}
