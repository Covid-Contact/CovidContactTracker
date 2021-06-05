package cat.covidcontact.tracker.feature.settings

import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.UserDevice
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.deleteaccount.DeleteAccount
import cat.covidcontact.usecases.makelogout.MakeLogOut
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val makeLogOut: MakeLogOut,
    private val deleteAccount: DeleteAccount
) : BaseViewModel() {
    private val makeLogOutHandler = UseCaseResultHandler<MakeLogOut.Response>(
        onSuccess = { SettingsState.NavigateToLogin },
        onFailure = { ScreenState.OtherError }
    )

    private val deleteAccountHandler = UseCaseResultHandler<DeleteAccount.Response>(
        onSuccess = { SettingsState.NavigateToLogin },
        onFailure = { ScreenState.OtherError }
    )

    fun onMakeLogOut(userDevice: UserDevice) {
        viewModelScope.launch {
            executeUseCase(makeLogOut, makeLogOutHandler) {
                MakeLogOut.Request(userDevice.user.email, userDevice.device.id)
            }
        }
    }

    fun onDeleteAccount(email: String) {
        viewModelScope.launch {
            executeUseCase(deleteAccount, deleteAccountHandler) {
                DeleteAccount.Request(email)
            }
        }
    }
}
