package cat.covidcontact.tracker.feature.contactnetworks

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.ScreenState

sealed class ContactNetworksState : ScreenState() {
    object CreateContactNetwork : ContactNetworksState()
    class ContactNetworkCreated(val contactNetwork: ContactNetwork) : ContactNetworksState()
}
