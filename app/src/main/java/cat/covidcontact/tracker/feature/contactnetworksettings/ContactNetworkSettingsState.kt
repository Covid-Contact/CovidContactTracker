package cat.covidcontact.tracker.feature.contactnetworksettings

import cat.covidcontact.tracker.ScreenState

sealed class ContactNetworkSettingsState : ScreenState() {
    class AccessCodeGenerated(val accessCode: String) : ContactNetworkSettingsState()
}
