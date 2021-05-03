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

    suspend fun registerRead(deviceIds: Set<String>, time: Long)
}
