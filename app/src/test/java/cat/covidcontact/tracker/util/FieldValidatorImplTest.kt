package cat.covidcontact.tracker.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class FieldValidatorImplTest {
    private lateinit var fieldValidatorImpl: FieldValidatorImpl

    @Before
    fun setUp() {
        fieldValidatorImpl = FieldValidatorImpl()
    }

    @Test
    fun emailWithoutAt_isEmailValidReturnsFalse() {
        // When the isEmailValid method is called with an email that does not contain an at
        val isValid = fieldValidatorImpl.isEmailValid("johnDoe")

        // Then the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun emailWithoutDot_isEmailValidReturnsFalse() {
        // When the isEmailValid method is called with an email that does not contain a dot
        val isValid = fieldValidatorImpl.isEmailValid("johnDoe@gmail")

        // Then the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun emailWithAtAndDot_isEmailValidReturnsTrue() {
        // When the isEmailValid method is called with an email that contains an at and a dot
        val isValid = fieldValidatorImpl.isEmailValid("johnDoe@gmail.com")

        // Then the result is true
        assertThat(isValid, `is`(true))
    }

    @Test
    fun passwordWithoutOneUppercaseLetter_isPasswordValidReturnsFalse() {
        // When the isPasswordValid method is called with a password that does not contain an uppercase letter at least
        val isValid = fieldValidatorImpl.isPasswordValid("abc")

        // The the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun passwordWithoutOneLowercaseLetter_isPasswordValidReturnsFalse() {
        // When the isPasswordValid method is called with a password that does not contain an lowercase letter at least
        val isValid = fieldValidatorImpl.isPasswordValid("ABC")

        // The the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun passwordWithoutOneDigit_isPasswordValidReturnsFalse() {
        // When the isPasswordValid method is called with a password that does not contain a digit at least
        val isValid = fieldValidatorImpl.isPasswordValid("Abc")

        // The the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun passwordWithoutSpecialCharacter_isPasswordValidReturnsFalse() {
        // When the isPasswordValid method is called with a password that does not contain a special character at least
        val isValid = fieldValidatorImpl.isPasswordValid("Abc1234")

        // The the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun passwordWithUppercaseLetterLowercaseLetterDigitAndSpecialCharacter_isPasswordValidReturnsTrue() {
        // When the isPasswordValid method is called with a password that contains an uppercase letter, a lowercase
        // letter, a digit and a special character at least
        val isValid = fieldValidatorImpl.isPasswordValid("Abc1234$")

        // The the result is true
        assertThat(isValid, `is`(true))
    }
}