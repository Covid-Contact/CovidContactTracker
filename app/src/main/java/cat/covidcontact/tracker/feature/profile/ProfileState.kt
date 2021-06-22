package cat.covidcontact.tracker.feature.profile

import cat.covidcontact.tracker.ScreenState

sealed class ProfileState : ScreenState() {
    object ProfileEdited : ProfileState()
}
