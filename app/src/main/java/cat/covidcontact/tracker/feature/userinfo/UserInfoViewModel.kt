package cat.covidcontact.tracker.feature.userinfo

import androidx.navigation.NavDirections
import cat.covidcontact.model.user.Gender
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.allNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(

) : BaseViewModel() {
    private lateinit var user: User
    val currentUser: User?
        get() = if (::user.isInitialized) user else null

    var inputEmail: String? = null
    var inputUsername: String? = null
    var inputGender: Gender? = null
    var inputBirthDate: Long? = null

    fun onCheckBasicInfo() = allNotNull(inputEmail, inputUsername, inputGender, inputBirthDate)

    fun onBasicUserInfoIntroduced(navDirections: NavDirections) {
        val email = inputEmail ?: return
        val username = inputUsername ?: return
        val gender = inputGender ?: return
        val birthDate = inputBirthDate ?: return

        user = User(email, username, gender, birthDate)
        onNextFragment(navDirections)
    }

    fun onNextFragment(navDirections: NavDirections) {
        loadState(UserInfoState.LoadNextFragment(navDirections))
    }

    fun onPreviousFragment() {
        loadState(UserInfoState.LoadPreviousFragment)
    }

    fun onUserCreated() {

    }

    fun onLoadNothing() {
        loadState(ScreenState.Nothing)
    }
}
