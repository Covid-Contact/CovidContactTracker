package cat.covidcontact.tracker.feature.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.tracker.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(

) : BaseViewModel() {
    private val _contactNetworks = MutableLiveData<List<ContactNetwork>>(emptyList())
    val contactNetworks: LiveData<List<ContactNetwork>>
        get() = _contactNetworks
}
