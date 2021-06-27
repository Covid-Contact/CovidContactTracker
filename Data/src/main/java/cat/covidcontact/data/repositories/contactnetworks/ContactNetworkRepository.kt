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

package cat.covidcontact.data.repositories.contactnetworks

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User

interface ContactNetworkRepository {
    suspend fun createContactNetwork(name: String, password: String?, owner: User): ContactNetwork
    suspend fun getContactNetworks(email: String): List<ContactNetwork>
    suspend fun enableUserAddition(contactNetworkName: String, isEnabled: Boolean)
    suspend fun generateAccessCode(email: String, contactNetworkName: String): String
    suspend fun getContactNetworkByAccessCode(accessCode: String): ContactNetwork
    suspend fun joinContactNetwork(email: String, contactNetworkName: String)
    suspend fun exitContactNetwork(email: String, contactNetworkName: String)
    suspend fun deleteContactNetwork(contactNetworkName: String, email: String)
}
