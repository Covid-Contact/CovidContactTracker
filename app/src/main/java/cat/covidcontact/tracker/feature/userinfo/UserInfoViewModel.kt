package cat.covidcontact.tracker.feature.userinfo

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import cat.covidcontact.model.user.Gender
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.allNotNull
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.adduserdata.AddUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val addUserData: AddUserData
) : BaseViewModel() {
    private lateinit var user: User
    val currentUser: User?
        get() = if (::user.isInitialized) user else null

    var inputEmail: String? = null
    var inputUsername: String? = null
    var inputGender: Gender? = null
    var inputBirthDate: Long? = null

    private val addUserHandler = UseCaseResultHandler<AddUserData.Response>(
        onSuccess = { UserInfoState.UserInfoCreated(it.email) },
        onFailure = { ScreenState.Nothing }
    )

    fun onCheckBasicInfo() = allNotNull(inputEmail, inputUsername, inputGender, inputBirthDate)

    fun onBasicUserInfoIntroduced(navDirections: NavDirections) {
        val email = inputEmail ?: return
        val username = inputUsername ?: return
        val gender = inputGender ?: return
        val birthDate = inputBirthDate ?: return

        if (currentUser == null) {
            user = User(email, username, gender, birthDate)
        }

        onNextFragment(navDirections)
    }

    fun onNextFragment(navDirections: NavDirections) {
        loadState(UserInfoState.LoadNextFragment(navDirections))
    }

    fun onPreviousFragment() {
        loadState(UserInfoState.LoadPreviousFragment)
    }

    fun onUserCreated() {
        viewModelScope.launch {
            executeUseCase(addUserData, addUserHandler) {
                AddUserData.Request(user)
            }
        }
    }
}
