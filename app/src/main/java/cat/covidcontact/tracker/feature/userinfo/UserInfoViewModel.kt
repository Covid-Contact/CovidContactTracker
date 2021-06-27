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

package cat.covidcontact.tracker.feature.userinfo

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import cat.covidcontact.model.user.Gender
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.allNotNull
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.adduserdata.AddUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val addUserData: AddUserData
) : BaseViewModel() {
    private lateinit var user: User
    val currentUser: User?
        get() = if (::user.isInitialized) user else null

    var inputEmail: String? = null
    var inputUsername: String? = null
    var inputGender: Gender? = null
    var inputBirthDate: Long? = null

    private val addUserHandler = UseCaseResultHandler<AddUserData.Response>(
        onSuccess = { UserInfoState.UserInfoCreated(it.email) },
        onFailure = { ScreenState.Nothing }
    )

    fun onCheckBasicInfo() = allNotNull(inputEmail, inputUsername, inputGender, inputBirthDate)

    fun onBasicUserInfoIntroduced(navDirections: NavDirections) {
        val username = inputUsername ?: return
        val email = inputEmail ?: return
        val gender = inputGender ?: return
        val birthDate = inputBirthDate ?: return

        if (currentUser == null) {
            user = User(username, email, gender, birthDate)
        }

        onNextFragment(navDirections)
    }

    fun onNextFragment(navDirections: NavDirections) {
        loadState(UserInfoState.LoadNextFragment(navDirections))
    }

    fun onPreviousFragment() {
        loadState(UserInfoState.LoadPreviousFragment)
    }

    fun onUserCreated() {
        viewModelScope.launch {
            executeUseCase(addUserData, addUserHandler) {
                AddUserData.Request(currentUser!!)
            }
        }
    }
}
