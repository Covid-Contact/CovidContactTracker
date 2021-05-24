package cat.covidcontact.tracker.feature.contactnetworks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.usecases.createContactNetwork.CreateContactNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactNetworksViewModel @Inject constructor(
    private val createContactNetwork: CreateContactNetwork
) : BaseViewModel() {
    private val _contactNetworks = MutableLiveData<List<ContactNetwork>>()
    val contactNetworks: LiveData<List<ContactNetwork>>
        get() = _contactNetworks

    fun onCreateContactNetworkDialog() {
        loadState(ContactNetworksState.CreateContactNetwork)
    }

    fun onLoadContactNetworks(user: User) {
        _contactNetworks.value = user.contactNetworks
    }

    fun onShowContactNetworkSettings(contactNetwork: ContactNetwork) {
        loadState(ContactNetworksState.ShowContactNetworkSettings(contactNetwork))
    }

    fun onNotifyPositive() {

    }
}
