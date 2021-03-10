package cat.covidcontact.tracker.authactivity.login

import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.authactivity.model.User

sealed class LogInState : ScreenState() {
    object ChangeToSignUp : LogInState()
    class EmailNotFound(val email: String) : LogInState()
    object WrongPassword : LogInState()
    class EmailNotValidated(val email: String) : LogInState()
    class SuccessLogIn(val user: User) : LogInState()
}
