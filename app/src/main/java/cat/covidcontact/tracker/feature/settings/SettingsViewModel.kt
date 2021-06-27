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

package cat.covidcontact.tracker.feature.settings

import androidx.lifecycle.viewModelScope
import cat.covidcontact.model.UserDevice
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.deleteaccount.DeleteAccount
import cat.covidcontact.usecases.makelogout.MakeLogOut
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val makeLogOut: MakeLogOut,
    private val deleteAccount: DeleteAccount
) : BaseViewModel() {
    private val makeLogOutHandler = UseCaseResultHandler<MakeLogOut.Response>(
        onSuccess = { SettingsState.NavigateToLogin },
        onFailure = { ScreenState.OtherError }
    )

    private val deleteAccountHandler = UseCaseResultHandler<DeleteAccount.Response>(
        onSuccess = { SettingsState.NavigateToLogin },
        onFailure = { ScreenState.OtherError }
    )

    fun onMakeLogOut(userDevice: UserDevice) {
        viewModelScope.launch {
            executeUseCase(makeLogOut, makeLogOutHandler) {
                MakeLogOut.Request(userDevice.user.email, userDevice.device.id)
            }
        }
    }

    fun onDeleteAccount(email: String) {
        viewModelScope.launch {
            executeUseCase(deleteAccount, deleteAccountHandler) {
                DeleteAccount.Request(email)
            }
        }
    }
}
