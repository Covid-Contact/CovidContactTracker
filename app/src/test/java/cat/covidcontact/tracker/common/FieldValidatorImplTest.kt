/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cat.covidcontact.tracker.common

import cat.covidcontact.tracker.common.fieldvalidator.FieldValidatorImpl
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
    fun `email without at is invalid`() {
        // When the isEmailValid method is called with an email that does not contain an at
        val isValid = fieldValidatorImpl.isEmailValid("johnDoe")

        // Then the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun `email without dot is invalid`() {
        // When the isEmailValid method is called with an email that does not contain a dot
        val isValid = fieldValidatorImpl.isEmailValid("johnDoe@gmail")

        // Then the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun `email with at and dot is valid`() {
        // When the isEmailValid method is called with an email that contains an at and a dot
        val isValid = fieldValidatorImpl.isEmailValid("johnDoe@gmail.com")

        // Then the result is true
        assertThat(isValid, `is`(true))
    }

    @Test
    fun `password without uppercase letter is invalid`() {
        // When the isPasswordValid method is called with a password that does not contain an uppercase letter at least
        val isValid = fieldValidatorImpl.isPasswordValid("abc")

        // The the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun `password without lowercase letter is invalid`() {
        // When the isPasswordValid method is called with a password that does not contain an lowercase letter at least
        val isValid = fieldValidatorImpl.isPasswordValid("ABC")

        // The the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun `password without digit is invalid`() {
        // When the isPasswordValid method is called with a password that does not contain a digit at least
        val isValid = fieldValidatorImpl.isPasswordValid("Abc")

        // The the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun `password without special character is invalid`() {
        // When the isPasswordValid method is called with a password that does not contain a special character at least
        val isValid = fieldValidatorImpl.isPasswordValid("Abc1234")

        // The the result is false
        assertThat(isValid, `is`(false))
    }

    @Test
    fun `password with uppercase letter, lowercase letter, digit and special character is valid`() {
        // When the isPasswordValid method is called with a password that contains an uppercase letter, a lowercase
        // letter, a digit and a special character at least
        val isValid = fieldValidatorImpl.isPasswordValid("Abc1234$")

        // The the result is true
        assertThat(isValid, `is`(true))
    }
}
