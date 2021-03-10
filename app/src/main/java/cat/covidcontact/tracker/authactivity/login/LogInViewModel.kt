package cat.covidcontact.tracker.authactivity.login

import androidx.lifecycle.*
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.authactivity.login.usecases.MakeLogIn
import cat.covidcontact.tracker.data.UserException
import cat.covidcontact.tracker.usecase.UseCaseResultHandler
import cat.covidcontact.tracker.util.FieldValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val makeLogIn: MakeLogIn,
    private val fieldValidator: FieldValidator
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Loading)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    private val _anyEmptyField = MutableLiveData(false)
    val anyEmptyField: LiveData<Boolean>
        get() = _anyEmptyField

    private val _isEmailInvalid = MutableLiveData(false)
    val isEmailInvalid: LiveData<Boolean>
        get() = _isEmailInvalid

    private val _isPasswordInvalid = MutableLiveData(false)
    val isPasswordInvalid: LiveData<Boolean>
        get() = _isPasswordInvalid

    private val makeLogInHandler = UseCaseResultHandler<MakeLogIn.Response>(
        onSuccess = { LogInState.SuccessLogIn(it.user) },
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
        _screenState.value = LogInState.ChangeToSignUp
    }

    fun onMakeLogIn(email: String, password: String) {
        viewModelScope.launch {
            if (!areParametersValid(email, password)) return@launch

            val result = withContext(Dispatchers.IO) {
                makeLogIn.execute(MakeLogIn.Request(email, password))
            }

            _screenState.value = makeLogInHandler.getScreenState(result)
        }
    }

    private fun areParametersValid(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            _anyEmptyField.value = true
            return false
        }

        if (!onVerifyEmail(email) || !onVerifyPassword(password)) {
            return false
        }

        return true
    }

    fun onVerifyEmail(email: String): Boolean {
        if (!fieldValidator.isEmailValid(email)) {
            _isEmailInvalid.value = true
            return false
        }

        return true
    }

    fun onVerifyPassword(password: String): Boolean {
        if (!fieldValidator.isPasswordValid(password)) {
            _isPasswordInvalid.value = true
            return false
        }

        return true
    }
}
