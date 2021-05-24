package cat.covidcontact.tracker.feature.contactnetworks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.requireValue
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.notifypositive.NotifyPositive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactNetworksViewModel @Inject constructor(
    private val notifyPositive: NotifyPositive
) : BaseViewModel() {
    private val currentUser: MutableLiveData<User> = MutableLiveData()
    val contactNetworks = currentUser.map { user -> user.contactNetworks }

    private val notifyPositiveHandler = UseCaseResultHandler<NotifyPositive.Response>(
        onSuccess = { response ->
            currentUser.value = response.user
            ScreenState.Nothing
        },
        onFailure = { ScreenState.OtherError }
    )

    fun onCreateContactNetworkDialog() {
        loadState(ContactNetworksState.CreateContactNetwork)
    }

    fun onLoadContactNetworks(user: User) {
        currentUser.value = user
    }

    fun onShowContactNetworkSettings(contactNetwork: ContactNetwork) {
        loadState(ContactNetworksState.ShowContactNetworkSettings(contactNetwork))
    }

    fun onNotifyPositive() {
        viewModelScope.launch {
            executeUseCase(notifyPositive, notifyPositiveHandler) {
                NotifyPositive.Request(currentUser.requireValue())
            }
        }
    }
}
