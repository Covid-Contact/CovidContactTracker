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
        return put("$url/$contactNetworkName", listOf("isEnabled" to isEnabled))
    }
}
