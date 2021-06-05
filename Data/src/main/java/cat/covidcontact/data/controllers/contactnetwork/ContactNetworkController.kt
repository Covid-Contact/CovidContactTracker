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
