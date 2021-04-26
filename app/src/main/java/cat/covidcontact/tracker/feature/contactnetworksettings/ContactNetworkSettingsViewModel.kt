package cat.covidcontact.tracker.feature.contactnetworksettings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.combine
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.enableUserAddition.EnableUserAddition
import cat.covidcontact.usecases.generateAccessCode.GenerateAccessCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactNetworkSettingsViewModel @Inject constructor(
    private val enableUserAddition: EnableUserAddition,
    private val generateAccessCode: GenerateAccessCode
) : BaseViewModel() {
    val contactNetwork = MutableLiveData<ContactNetwork>()
    val isVisibleChecked = contactNetwork.map { it.isVisible }
    val isPasswordEnabled = isVisibleChecked.combine(contactNetwork) { isVisible, network ->
        isVisible != null && isVisible && !network?.password.isNullOrEmpty()
    }
    val isPasswordChecked = isPasswordEnabled.combine(contactNetwork) { isEnabled, network ->
        isEnabled != null && network != null && isEnabled && network.isPasswordProtected
    }

    val isAccessCodeGenerated = contactNetwork.map { it.accessCode != null }

    private val enableUserAdditionHandler = UseCaseResultHandler<EnableUserAddition.Response>(
        onSuccess = { ScreenState.Nothing },
        onFailure = { ScreenState.Nothing }
    )

    fun onEnableUsersAddition(isEnabled: Boolean) {
        viewModelScope.launch {
            contactNetwork.value?.let { contactNetwork ->
                executeUseCase(enableUserAddition, enableUserAdditionHandler, false) {
                    EnableUserAddition.Request(contactNetwork, isEnabled)
                }
            }
        }
    }

    fun onGenerateAccessCode() {

    }
}
