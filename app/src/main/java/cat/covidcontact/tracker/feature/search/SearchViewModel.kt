package cat.covidcontact.tracker.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkException
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.requireValue
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.getContactNetworkByAccessCode.GetContactNetworkByAccessCode
import cat.covidcontact.usecases.joinContactNetwork.JoinContactNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getContactNetworkByAccessCode: GetContactNetworkByAccessCode,
    private val joinContactNetwork: JoinContactNetwork
) : BaseViewModel() {
    private val _contactNetworks = MutableLiveData<List<ContactNetwork>>(emptyList())
    val contactNetworks: LiveData<List<ContactNetwork>>
        get() = _contactNetworks

    val isNoContactNetworksVisible = _contactNetworks.map { it.isEmpty() }

    val accessCode = MutableLiveData("")
    val isSearchByAccessCodeEnabled = accessCode.map { accessCode -> accessCode.length == 6 }

    private val getContactNetworkByAccessCodeHandler =
        UseCaseResultHandler<GetContactNetworkByAccessCode.Response>(
            onSuccess = {
                _contactNetworks.value = listOf(it.contactNetwork)
                ScreenState.Nothing
            },
            onFailure = { ScreenState.Nothing }
        )

    private val joinContactNetworkHandler = UseCaseResultHandler<JoinContactNetwork.Response>(
        onSuccess = {
            _contactNetworks.value = emptyList()
            SearchState.ContactNetworkJoined(it.contactNetworkName)
        },
        onFailure = { exception ->
            when (exception) {
                is ContactNetworkException.AlreadyJoined -> {
                    _contactNetworks.value = emptyList()
                    SearchState.AlreadyJoined(exception.contactNetworkName)
                }
                else -> ScreenState.Nothing
            }
        }
    )

    fun onGetContactNetworkByAccessCode() {
        viewModelScope.launch {
            executeUseCase(getContactNetworkByAccessCode, getContactNetworkByAccessCodeHandler) {
                GetContactNetworkByAccessCode.Request(accessCode.requireValue())
            }
        }
    }

    fun onJoinContactNetwork(user: User, contactNetwork: ContactNetwork) {
        viewModelScope.launch {
            executeUseCase(joinContactNetwork, joinContactNetworkHandler) {
                JoinContactNetwork.Request(user, contactNetwork)
            }
        }
    }
}
