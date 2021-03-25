package cat.covidcontact.tracker.util.fieldvalidator

import android.util.Patterns

class FieldValidatorImpl : FieldValidator {
    override fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun isPasswordValid(password: String): Boolean {
        val regex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[-+_!@#\$%^&€*.,?()/\\\\|¿¡=~ñç¬{}])"
        return password.length >= 8 && password.contains(Regex(regex))
    }
}