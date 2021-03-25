package cat.covidcontact.tracker.authactivity.login

import androidx.lifecycle.*
import cat.covidcontact.data.UserException
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.util.BaseViewModel
import cat.covidcontact.tracker.util.fieldvalidator.FieldValidator
import cat.covidcontact.tracker.util.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.login.MakeLogIn
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
