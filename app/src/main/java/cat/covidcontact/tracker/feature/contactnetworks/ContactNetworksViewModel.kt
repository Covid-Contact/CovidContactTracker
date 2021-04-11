package cat.covidcontact.tracker.feature.contactnetworks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.createContactNetwork.CreateContactNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactNetworksViewModel @Inject constructor(
    private val createContactNetwork: CreateContactNetwork
) : BaseViewModel() {
    private val _isNameEmpty = MutableLiveData<Boolean>()
    val isNameEmpty: LiveData<Boolean>
        get() = _isNameEmpty

    private val createContactNetworkHandler = UseCaseResultHandler<CreateContactNetwork.Response>(
        onSuccess = { ContactNetworksState.ContactNetworkCreated(it.contactNetwork) },
        onFailure = { ScreenState.Nothing }
    )

    fun onCreateContactNetworkDialog() {
        loadState(ContactNetworksState.CreateContactNetwork)
    }

    fun onCreateContactNetwork(name: String, password: String?, owner: User) {
        viewModelScope.launch {
            executeUseCase(createContactNetwork, createContactNetworkHandler) {
                CreateContactNetwork.Request(name, password, owner)
            }
        }
    }

    fun onValidateName(name: String): Boolean {
        _isNameEmpty.value = name.isEmpty()
        return !_isNameEmpty.value!!
    }
}
