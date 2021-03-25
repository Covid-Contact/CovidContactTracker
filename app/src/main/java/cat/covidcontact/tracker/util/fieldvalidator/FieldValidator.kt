package cat.covidcontact.tracker.util.fieldvalidator

interface FieldValidator {
    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
}