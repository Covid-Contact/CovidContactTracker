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

package cat.covidcontact.data.controllers.contactnetwork

import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.post.PostContactNetwork

abstract class ContactNetworkController : CovidContactBaseController() {
    override val url = "${super.url}/contactnetwork"

    abstract suspend fun createContactNetwork(
        postContactNetwork: PostContactNetwork
    ): ServerResponse

    abstract suspend fun getContactNetworks(
        email: String
    ): ServerResponse

    abstract suspend fun enableUserAddition(
        contactNetworkName: String,
        isEnabled: Boolean
    ): ServerResponse

    abstract suspend fun generateAccessCode(
        email: String,
        contactNetworkName: String
    ): ServerResponse

    abstract suspend fun getContactNetworkByAccessCode(accessCode: String): ServerResponse

    abstract suspend fun joinContactNetwork(
        email: String,
        contactNetworkName: String
    ): ServerResponse

    abstract suspend fun exitContactNetwork(
        email: String,
        contactNetworkName: String
    ): ServerResponse

    abstract suspend fun deleteContactNetwork(
        email: String,
        contactNetworkName: String
    ): ServerResponse
}
