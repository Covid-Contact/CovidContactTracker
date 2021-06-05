package cat.covidcontact.tracker.common.fieldvalidator

interface FieldValidator {
    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
}