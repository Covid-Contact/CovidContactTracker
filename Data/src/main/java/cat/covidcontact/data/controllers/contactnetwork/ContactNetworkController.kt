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
}
