package cat.covidcontact.tracker.feature.contactnetworks

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.ScreenState

sealed class ContactNetworksState : ScreenState() {
    object CreateContactNetwork : ContactNetworksState()
    class ShowContactNetworkSettings(val contactNetwork: ContactNetwork) : ContactNetworksState()
    object ExitContactNetwork : ContactNetworksState()
}
