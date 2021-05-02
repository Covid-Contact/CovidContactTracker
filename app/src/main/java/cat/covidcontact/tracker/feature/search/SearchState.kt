package cat.covidcontact.tracker.feature.search

import cat.covidcontact.tracker.ScreenState

sealed class SearchState : ScreenState() {
    class ContactNetworkJoined(val contactNetworkName: String) : SearchState()
    class AlreadyJoined(val contactNetworkName: String) : SearchState()
}
