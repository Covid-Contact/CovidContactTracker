package cat.covidcontact.tracker.feature.contactnetworksettings

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.ScreenState

sealed class ContactNetworkSettingsState : ScreenState() {
    class AccessCodeGenerated(val accessCode: String) : ContactNetworkSettingsState()
    class ContactNetworkDeleted(val contactNetwork: ContactNetwork) : ContactNetworkSettingsState()
}
