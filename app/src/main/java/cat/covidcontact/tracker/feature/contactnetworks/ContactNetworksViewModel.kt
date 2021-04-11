package cat.covidcontact.tracker.feature.contactnetworks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cat.covidcontact.tracker.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactNetworksViewModel @Inject constructor(

) : BaseViewModel() {
    private val _isNameEmpty = MutableLiveData<Boolean>()
    val isNameEmpty: LiveData<Boolean>
        get() = _isNameEmpty

    fun onCreateContactNetworkDialog() {
        loadState(ContactNetworksState.CreateContactNetwork)
    }

    fun onCreateContactNetwork(name: String, password: String, ownerUsername: String) {
        viewModelScope.launch {

        }
    }

    fun onValidateName(name: String): Boolean {
        _isNameEmpty.value = name.isEmpty()
        return !_isNameEmpty.value!!
    }
}
