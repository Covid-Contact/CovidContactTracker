package cat.covidcontact.tracker.feature.contactnetworks

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.ScreenState

sealed class ContactNetworksState : ScreenState() {
    object CreateContactNetwork : ContactNetworksState()
    class ContactNetworkAlreadyExisting(val name: String) : ContactNetworksState()
    class ContactNetworkCreated(val contactNetwork: ContactNetwork) : ContactNetworksState()
}
