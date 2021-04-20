package cat.covidcontact.tracker.feature.contactnetworksettings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.enableUserAddition.EnableUserAddition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactNetworkSettingsViewModel @Inject constructor(
    private val enableUserAddition: EnableUserAddition
) : BaseViewModel() {
    val contactNetwork = MutableLiveData<ContactNetwork>()
    val isVisibleChecked = contactNetwork.map { it.isVisible }

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
}
