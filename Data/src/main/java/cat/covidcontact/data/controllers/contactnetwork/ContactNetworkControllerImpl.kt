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
        val name = modifyInvalidCharacters(contactNetworkName)
        return put("$url/$name", listOf("isEnabled" to isEnabled))
    }

    override suspend fun generateAccessCode(
        email: String,
        contactNetworkName: String
    ): ServerResponse {
        val name = modifyInvalidCharacters(contactNetworkName)
        return put("$url/$name/generateAccessCode")
    }

    private fun modifyInvalidCharacters(contactNetworkName: String) =
        contactNetworkName.replace(" ", "%20").replace("#", "%23")
}
