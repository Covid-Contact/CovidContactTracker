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

package cat.covidcontact.tracker.feature.login

import androidx.lifecycle.*
import cat.covidcontact.data.repositories.user.UserException
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.fieldvalidator.FieldValidator
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.makelogin.MakeLogIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val makeLogIn: MakeLogIn,
    private val fieldValidator: FieldValidator
) : BaseViewModel() {

    private val _isEmailInvalid = MutableLiveData<Boolean>()
    val isEmailInvalid: LiveData<Boolean>
        get() = _isEmailInvalid

    private val _isPasswordInvalid = MutableLiveData<Boolean>()
    val isPasswordInvalid: LiveData<Boolean>
        get() = _isPasswordInvalid

    private val makeLogInHandler = UseCaseResultHandler<MakeLogIn.Response>(
        onSuccess = { LogInState.SuccessLogIn(it.email) },
        onFailure = { exception ->
            when (exception) {
                is UserException.EmailNotFoundException -> LogInState.EmailNotFound(
                    exception.email
                )
                UserException.WrongPasswordException -> LogInState.WrongPassword
                is UserException.EmailNotValidatedException -> LogInState.EmailNotValidated(
                    exception.email
                )
                else -> ScreenState.OtherError
            }
        }
    )

    fun onChangeToSignUp() {
        loadState(LogInState.ChangeToSignUp)
    }

    fun onMakeLogIn(email: String, password: String) {
        viewModelScope.launch {
            if (!areParametersValid(email, password)) return@launch

            executeUseCase(makeLogIn, makeLogInHandler) {
                MakeLogIn.Request(email, password)
            }
        }
    }

    private fun areParametersValid(email: String, password: String) =
        onVerifyEmail(email) and onVerifyPassword(password)

    fun onVerifyEmail(email: String): Boolean {
        _isEmailInvalid.value = !fieldValidator.isEmailValid(email)
        return !isEmailInvalid.value!!
    }

    fun onVerifyPassword(password: String): Boolean {
        _isPasswordInvalid.value = !fieldValidator.isPasswordValid(password)
        return !isPasswordInvalid.value!!
    }
}
