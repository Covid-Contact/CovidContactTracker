package cat.covidcontact.tracker.util

interface FieldValidator {
    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
}