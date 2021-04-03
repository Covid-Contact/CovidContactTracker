package cat.covidcontact.tracker.feature.userinfo

import androidx.navigation.NavDirections
import cat.covidcontact.tracker.ScreenState

sealed class UserInfoState : ScreenState() {
    class LoadNextFragment(val navDirections: NavDirections) : UserInfoState()
    object LoadPreviousFragment : UserInfoState()
    class UserInfoIntroduced(val navDirections: NavDirections) : UserInfoState()
    class UserInfoCreated(val email: String) : UserInfoState()
}
