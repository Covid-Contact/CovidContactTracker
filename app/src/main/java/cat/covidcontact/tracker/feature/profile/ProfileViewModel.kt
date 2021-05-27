package cat.covidcontact.tracker.feature.profile

import androidx.lifecycle.MutableLiveData
import cat.covidcontact.tracker.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : BaseViewModel() {
    val city = MutableLiveData("")
    val studies = MutableLiveData("")
    val occupation = MutableLiveData("")
    val marriage = MutableLiveData("")
    val children = MutableLiveData("")
    val positive = MutableLiveData("")
    val vaccinated = MutableLiveData("")
}
