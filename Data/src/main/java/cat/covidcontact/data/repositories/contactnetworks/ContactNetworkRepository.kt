package cat.covidcontact.data.repositories.contactnetworks

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User

interface ContactNetworkRepository {
    suspend fun createContactNetwork(name: String, password: String?, owner: User): ContactNetwork
    suspend fun getContactNetworks(email: String): List<ContactNetwork>
    suspend fun enableUserAddition(contactNetworkName: String, isEnabled: Boolean)
}
