package cat.covidcontact.tracker.common.fieldvalidator

import java.util.regex.Pattern

class FieldValidatorImpl : FieldValidator {
    val emailAddressPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )

    override fun isEmailValid(email: String): Boolean {
        return emailAddressPattern.matcher(email).matches()
    }

    override fun isPasswordValid(password: String): Boolean {
        val regex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[-+_!@#\$%^&€*.,?()/\\\\|¿¡=~ñç¬{}])"
        return password.length >= 8 && password.contains(Regex(regex))
    }
}