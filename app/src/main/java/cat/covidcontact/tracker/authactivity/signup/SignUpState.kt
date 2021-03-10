package cat.covidcontact.tracker.authactivity.signup

import cat.covidcontact.tracker.ScreenState

sealed class SignUpState() : ScreenState() {
    object ChangeToLogIn : SignUpState()
    class EmailAlreadyRegistered(val email: String) : SignUpState()
    class VerifyCodeSent(val email: String) : SignUpState()
}
