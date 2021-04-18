package cat.covidcontact.tracker.feature.createcontactnetwork

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.ScreenState

sealed class CreateContactNetworkState : ScreenState() {
    class ContactNetworkAlreadyExisting(val name: String) : CreateContactNetworkState()
    class ContactNetworkCreated(val contactNetwork: ContactNetwork) : CreateContactNetworkState()
}
