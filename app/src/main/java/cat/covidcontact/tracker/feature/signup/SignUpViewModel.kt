package cat.covidcontact.tracker.feature.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cat.covidcontact.data.UserException
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