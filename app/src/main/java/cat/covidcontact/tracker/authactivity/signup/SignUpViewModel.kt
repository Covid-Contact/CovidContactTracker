package cat.covidcontact.tracker.authactivity.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.covidcontact.data.UserException
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.util.fieldvalidator.FieldValidator
import cat.covidcontact.tracker.util.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.signup.MakeSignUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val makeSignUp: MakeSignUp,
    private val fieldValidator: FieldValidator
) : ViewModel() {
    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Loading)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _anyEmptyField = MutableLiveData<Boolean>()
    val anyEmptyField: LiveData<Boolean>
        get() = _anyEmptyField

    private val _isEmailInvalid = MutableLiveData<Boolean>()
    val isEmailInvalid: LiveData<Boolean>
        get() = _isEmailInvalid

    private val _isPasswordInvalid = MutableLiveData<Boolean>()
    val isPasswordInvalid: LiveData<Boolean>
        get() = _isPasswordInvalid

    private val _arePasswordsEquals = MutableLiveData<Boolean>()
    val arePasswordsEquals: LiveData<Boolean>
        get() = _arePasswordsEquals

    private val makeSignUpHandler = UseCaseResultHandler<MakeSignUp.Response>(
        onSuccess = { SignUpState.VerifyCodeSent(it.email) },
        onFailure = {
            val email = (it as UserException.EmailAlreadyRegistered).email
            SignUpState.EmailAlreadyRegistered(email)
        }
    )

    fun onChangeToLogIn() {
        _screenState.value = SignUpState.ChangeToLogIn
    }

    fun onMakeSignUp(email: String, password: String, repeatedPassword: String) {
        viewModelScope.launch {
            if (!areParametersValid(email, password, repeatedPassword)) return@launch

            val result = withContext(Dispatchers.IO) {
                makeSignUp.execute(MakeSignUp.Request(email, password))
            }

            _screenState.value = makeSignUpHandler.getScreenState(result)
        }
    }

    private fun areParametersValid(
        email: String,
        password: String,
        repeatedPassword: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            _anyEmptyField.value = true
            return false
        }

        return onVerifyEmail(email) && onVerifyPassword(password)
            && onVerifyRepeatedPasswords(password, repeatedPassword)
    }

    fun onVerifyEmail(email: String): Boolean {
        _isEmailInvalid.value = !fieldValidator.isEmailValid(email)
        return !isEmailInvalid.value!!
    }

    fun onVerifyPassword(password: String): Boolean {
        _isPasswordInvalid.value = !fieldValidator.isPasswordValid(password)
        return !isPasswordInvalid.value!!
    }

    fun onVerifyRepeatedPasswords(password: String, repeatedPassword: String): Boolean {
        _arePasswordsEquals.value = password == repeatedPassword
        return arePasswordsEquals.value!!
    }
}