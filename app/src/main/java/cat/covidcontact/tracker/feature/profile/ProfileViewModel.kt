package cat.covidcontact.tracker.feature.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.requireValue
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.updateprofile.UpdateProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val updateProfile: UpdateProfile
) : BaseViewModel() {
    val city = MutableLiveData("")
    val studies = MutableLiveData("")
    val occupation = MutableLiveData("")
    val marriage = MutableLiveData("")
    val children = MutableLiveData("")
    val positive = MutableLiveData<Boolean?>(null)
    val vaccinated = MutableLiveData<Boolean?>(null)

    private val updateProfileHandler = UseCaseResultHandler<UpdateProfile.Response>(
        onSuccess = { ScreenState.Nothing },
        onFailure = { ScreenState.OtherError }
    )

    fun onUpdateProfile(user: User) {
        viewModelScope.launch {
            executeUseCase(updateProfile, updateProfileHandler) {
                UpdateProfile.Request(
                    user,
                    city.requireValue(),
                    studies.requireValue(),
                    occupation.requireValue(),
                    marriage.requireValue(),
                    if (children.requireValue().isNotEmpty()) children.requireValue()
                        .toInt() else 0,
                    positive.value,
                    vaccinated.value
                )
            }
        }
    }
}
