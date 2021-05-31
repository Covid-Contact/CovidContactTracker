package cat.covidcontact.tracker.feature.contactnetworks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.notify
import cat.covidcontact.tracker.common.extensions.requireValue
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.exitcontactnetwork.ExitContactNetwork
import cat.covidcontact.usecases.notifypositive.NotifyPositive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactNetworksViewModel @Inject constructor(
    private val notifyPositive: NotifyPositive,
    private val exitContactNetwork: ExitContactNetwork
) : BaseViewModel() {
    private val currentUser: MutableLiveData<User> = MutableLiveData()
    private val _contactNetworks = MutableLiveData<List<ContactNetwork>>()
    val contactNetworks: LiveData<List<ContactNetwork>>
        get() = _contactNetworks

    private val notifyPositiveHandler = UseCaseResultHandler<NotifyPositive.Response>(
        onSuccess = { response ->
            currentUser.value = response.user
            currentUser.notify()
            ScreenState.Nothing
        },
        onFailure = { ScreenState.OtherError }
    )

    private val exitContactNetworkHandler = UseCaseResultHandler<ExitContactNetwork.Response>(
        onSuccess = { response ->
            _contactNetworks.value = listOf()
            _contactNetworks.value = response.user.contactNetworks
            ScreenState.Nothing
        },
        onFailure = { ScreenState.OtherError }
    )

    fun onCreateContactNetworkDialog() {
        loadState(ContactNetworksState.CreateContactNetwork)
    }

    fun onLoadContactNetworks(user: User) {
        currentUser.value = user
        _contactNetworks.value = user.contactNetworks
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

    fun onExitContactNetwork(contactNetwork: ContactNetwork) {
        viewModelScope.launch {
            executeUseCase(exitContactNetwork, exitContactNetworkHandler) {
                ExitContactNetwork.Request(currentUser.requireValue(), contactNetwork)
            }
        }
    }
}
