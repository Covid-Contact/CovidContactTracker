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

import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.post.PostContactNetwork

class ContactNetworkControllerImpl : ContactNetworkController() {

    override suspend fun createContactNetwork(
        postContactNetwork: PostContactNetwork
    ): ServerResponse {
        return post("$url/", postContactNetwork)
    }

    override suspend fun getContactNetworks(email: String): ServerResponse {
        return get("$url/", listOf("email" to email))
    }

    override suspend fun enableUserAddition(
        contactNetworkName: String,
        isEnabled: Boolean
    ): ServerResponse {
        val name = contactNetworkName.modifyInvalidCharacters()
        return put("$url/$name", listOf("isEnabled" to isEnabled))
    }

    override suspend fun generateAccessCode(
        email: String,
        contactNetworkName: String
    ): ServerResponse {
        val name = contactNetworkName.modifyInvalidCharacters()
        return put("$url/$name/generateAccessCode")
    }

    override suspend fun getContactNetworkByAccessCode(accessCode: String): ServerResponse {
        return get("$url/accessCode", listOf("code" to accessCode))
    }

    override suspend fun joinContactNetwork(
        email: String,
        contactNetworkName: String
    ): ServerResponse {
        val name = contactNetworkName.modifyInvalidCharacters()
        return put("$url/$name/join", listOf("email" to email))
    }

    override suspend fun exitContactNetwork(
        email: String,
        contactNetworkName: String
    ): ServerResponse {
        val name = contactNetworkName.modifyInvalidCharacters()
        return delete("$url/$name/exit", listOf("email" to email))
    }

    override suspend fun deleteContactNetwork(
        email: String,
        contactNetworkName: String
    ): ServerResponse {
        val name = contactNetworkName.modifyInvalidCharacters()
        return delete("$url/$name/delete", listOf("email" to email))
    }

    private fun String.modifyInvalidCharacters() = replace(" ", "%20").replace("#", "%23")
}
