package cat.covidcontact.tracker.feature.contactnetworksettings

import androidx.lifecycle.MutableLiveData
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactNetworkSettingsViewModel @Inject constructor(

) : BaseViewModel() {
    val contactNetwork = MutableLiveData<ContactNetwork>()
}
