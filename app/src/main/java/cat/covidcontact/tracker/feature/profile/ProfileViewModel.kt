/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
