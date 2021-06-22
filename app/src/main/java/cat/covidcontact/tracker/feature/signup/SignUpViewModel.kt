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

package cat.covidcontact.tracker.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cat.covidcontact.data.repositories.user.UserException
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.fieldvalidator.FieldValidator
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.signup.MakeSignUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val makeSignUp: MakeSignUp,
    private val fieldValidator: FieldValidator
) : BaseViewModel() {
    private val _isEmailInvalid = MutableLiveData<Boolean>()
    val isEmailInvalid: LiveData<Boolean>
        get() = _isEmailInvalid

    private val _isPasswordInvalid = MutableLiveData<Boolean>()
    val isPasswordInvalid: LiveData<Boolean>
        get() = _isPasswordInvalid

    private val _arePasswordsDiferent = MutableLiveData<Boolean>()
    val arePasswordsDiferent: LiveData<Boolean>
        get() = _arePasswordsDiferent

    private lateinit var currentPassword: String

    private val makeSignUpHandler = UseCaseResultHandler<MakeSignUp.Response>(
        onSuccess = { SignUpState.VerifyEmailSent(it.email) },
        onFailure = {
            val email = (it as UserException.EmailAlreadyRegistered).email
            SignUpState.EmailAlreadyRegistered(email)
        }
    )

    fun onChangeToLogIn() {
        loadState(SignUpState.ChangeToLogIn)
    }

    fun onMakeSignUp(email: String, password: String, repeatedPassword: String) {
        viewModelScope.launch {
            if (!areParametersValid(email, password, repeatedPassword)) return@launch

            executeUseCase(makeSignUp, makeSignUpHandler) {
                MakeSignUp.Request(email, password)
            }
        }
    }

    private fun areParametersValid(email: String, password: String, repeatedPassword: String) =
        onVerifyEmail(email) and onVerifyPassword(password) and
            onVerifyRepeatedPassword(repeatedPassword)

    fun onVerifyEmail(email: String): Boolean {
        _isEmailInvalid.value = !fieldValidator.isEmailValid(email)
        return !isEmailInvalid.value!!
    }

    fun onVerifyPassword(password: String): Boolean {
        currentPassword = password
        _isPasswordInvalid.value = !fieldValidator.isPasswordValid(password)
        return !isPasswordInvalid.value!!
    }

    fun onVerifyRepeatedPassword(repeatedPassword: String): Boolean {
        _arePasswordsDiferent.value = currentPassword != repeatedPassword
        return !arePasswordsDiferent.value!!
    }
}
