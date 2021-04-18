package cat.covidcontact.tracker.feature.createcontactnetwork

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
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
class CreateContactNetworkViewModel @Inject constructor(
    private val createContactNetwork: CreateContactNetwork
) : BaseViewModel() {
    val name = MutableLiveData("")
    val password = MutableLiveData("")

    val isNameValid = name.map { it.isNotEmpty() }

    private val createContactNetworkHandler = UseCaseResultHandler<CreateContactNetwork.Response>(
        onSuccess = { CreateContactNetworkState.ContactNetworkCreated(it.contactNetwork) },
        onFailure = { ScreenState.Nothing }
    )

    fun onCreateContactNetwork(owner: User) {
        viewModelScope.launch {
            executeUseCase(createContactNetwork, createContactNetworkHandler) {
                CreateContactNetwork.Request(name.value!!, password.value!!, owner)
            }
        }
    }
}
