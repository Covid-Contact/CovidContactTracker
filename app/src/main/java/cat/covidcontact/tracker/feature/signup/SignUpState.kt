package cat.covidcontact.tracker.feature.signup

import cat.covidcontact.tracker.ScreenState

sealed class SignUpState() : ScreenState() {
    object ChangeToLogIn : SignUpState()
    class EmailAlreadyRegistered(val email: String) : SignUpState()
    class VerifyEmailSent(val email: String) : SignUpState()
}
