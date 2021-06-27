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
