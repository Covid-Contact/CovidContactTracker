package cat.covidcontact.tracker.feature.login

import cat.covidcontact.tracker.ScreenState

sealed class LogInState : ScreenState() {
    object ChangeToSignUp : LogInState()
    class EmailNotFound(val email: String) : LogInState()
    object WrongPassword : LogInState()
    class EmailNotValidated(val email: String) : LogInState()
    class SuccessLogIn(val email: String) : LogInState()
}
