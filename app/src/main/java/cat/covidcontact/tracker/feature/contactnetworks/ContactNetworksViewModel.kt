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

package cat.covidcontact.tracker.feature.contactnetworks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.notify
import cat.covidcontact.tracker.common.extensions.requireValue
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.exitcontactnetwork.ExitContactNetwork
import cat.covidcontact.usecases.notifypositive.NotifyPositive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactNetworksViewModel @Inject constructor(
    private val notifyPositive: NotifyPositive,
    private val exitContactNetwork: ExitContactNetwork
) : BaseViewModel() {
    private val currentUser: MutableLiveData<User> = MutableLiveData()
    private val _contactNetworks = MutableLiveData<List<ContactNetwork>>()
    val contactNetworks: LiveData<List<ContactNetwork>>
        get() = _contactNetworks

    val isNoContactNetworksVisible = _contactNetworks.map { it.isEmpty() }

    private val notifyPositiveHandler = UseCaseResultHandler<NotifyPositive.Response>(
        onSuccess = { response ->
            currentUser.value = response.user
            currentUser.notify()
            ScreenState.Nothing
        },
        onFailure = { ScreenState.OtherError }
    )

    private val exitContactNetworkHandler = UseCaseResultHandler<ExitContactNetwork.Response>(
        onSuccess = { response ->
            _contactNetworks.value = listOf()
            _contactNetworks.value = response.user.contactNetworks
            ContactNetworksState.ExitContactNetwork
        },
        onFailure = { ScreenState.OtherError }
    )

    fun onCreateContactNetworkDialog() {
        loadState(ContactNetworksState.CreateContactNetwork)
    }

    fun onLoadContactNetworks(user: User) {
        currentUser.value = user
        _contactNetworks.value = user.contactNetworks
    }

    fun onShowContactNetworkSettings(contactNetwork: ContactNetwork) {
        loadState(ContactNetworksState.ShowContactNetworkSettings(contactNetwork))
    }

    fun onNotifyPositive() {
        viewModelScope.launch {
            executeUseCase(notifyPositive, notifyPositiveHandler) {
                NotifyPositive.Request(currentUser.requireValue())
            }
        }
    }

    fun onExitContactNetwork(contactNetwork: ContactNetwork) {
        viewModelScope.launch {
            executeUseCase(exitContactNetwork, exitContactNetworkHandler) {
                ExitContactNetwork.Request(currentUser.requireValue(), contactNetwork)
            }
        }
    }
}
