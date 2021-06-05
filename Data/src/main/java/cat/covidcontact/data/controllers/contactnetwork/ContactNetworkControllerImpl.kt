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
