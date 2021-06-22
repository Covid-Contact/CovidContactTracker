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

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.contactnetwork.ContactNetworkController
import cat.covidcontact.data.fromJson
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.post.PostContactNetwork
import cat.covidcontact.model.user.User
import com.google.gson.Gson
import java.security.MessageDigest
import javax.inject.Inject

class ContactNetworkRepositoryImpl @Inject constructor(
    private val contactNetworkController: ContactNetworkController
) : ContactNetworkRepository {

    override suspend fun createContactNetwork(
        name: String,
        password: String?,
        owner: User
    ): ContactNetwork {
        val serverResponse = contactNetworkController.createContactNetwork(
            PostContactNetwork(
                name = name,
                password = password?.hashPassword(),
                ownerEmail = owner.email,
                ownerUsername = owner.username
            )
        )

        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@onStatusCode
                HttpStatus.BAD_REQUEST ->
                    throw ContactNetworkException.ContactNetworkAlreadyExisting
                else -> throw CommonException.OtherError
            }
        }

        val postContactNetwork = Gson().fromJson(
            serverResponse.result.get(),
            PostContactNetwork::class.java
        )

        return ContactNetwork.fromPost(postContactNetwork)
    }

    override suspend fun getContactNetworks(email: String): List<ContactNetwork> {
        val serverResponse = contactNetworkController.getContactNetworks(email)

        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }

        val contactNetworks = Gson().fromJson<List<PostContactNetwork>>(
            serverResponse.result.get()
        )
        return contactNetworks.map { ContactNetwork.fromPost(it) }
    }

    override suspend fun enableUserAddition(contactNetworkName: String, isEnabled: Boolean) {
        val serverResponse = contactNetworkController.enableUserAddition(
            contactNetworkName,
            isEnabled
        )

        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@onStatusCode
                HttpStatus.NOT_FOUND -> throw ContactNetworkException.ContactNetworkNotExisting
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun generateAccessCode(email: String, contactNetworkName: String): String {
        val serverResponse = contactNetworkController.generateAccessCode(email, contactNetworkName)

        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }

        return serverResponse.result.get()
    }

    override suspend fun getContactNetworkByAccessCode(accessCode: String): ContactNetwork {
        val serverResponse = contactNetworkController.getContactNetworkByAccessCode(accessCode)
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@onStatusCode
                HttpStatus.NOT_FOUND -> throw ContactNetworkException.ContactNetworkNotExisting
                else -> throw CommonException.OtherError
            }
        }

        val contactNetwork = Gson().fromJson<PostContactNetwork>(
            serverResponse.result.get()
        )
        return ContactNetwork.fromPost(contactNetwork)
    }

    override suspend fun joinContactNetwork(email: String, contactNetworkName: String) {
        val serverResponse = contactNetworkController.joinContactNetwork(email, contactNetworkName)
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@onStatusCode
                HttpStatus.BAD_REQUEST -> throw ContactNetworkException.AlreadyJoined(
                    contactNetworkName
                )
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun exitContactNetwork(email: String, contactNetworkName: String) {
        val serverResponse = contactNetworkController.exitContactNetwork(email, contactNetworkName)
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun deleteContactNetwork(contactNetworkName: String, email: String) {
        val serverResponse = contactNetworkController.deleteContactNetwork(
            email,
            contactNetworkName
        )

        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }
    }

    private fun String.hashPassword(): String {
        return MessageDigest.getInstance("SHA-256")
            .digest(toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
    }
}
