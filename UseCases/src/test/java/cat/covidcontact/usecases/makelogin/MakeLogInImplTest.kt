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

package cat.covidcontact.usecases.makelogin

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.user.UserException
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.*
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MakeLogInImplTest {
    private lateinit var useCase: MakeLogInImpl

    private val request = MakeLogIn.Request(EMAIL, PASSWORD)

    @MockK
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery { userRepository.validateAndMakeLogIn(any(), any()) } returns Unit

        useCase = MakeLogInImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                userRepository.validateAndMakeLogIn(any(), any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when password is wrong then use case fails`() =
        runErrorTest(useCase, request, UserException.WrongPasswordException::class) {
            coEvery {
                userRepository.validateAndMakeLogIn(any(), any())
            } throws UserException.WrongPasswordException
        }

    @Test
    fun `when email not found then use case fails`() =
        runErrorTest(useCase, request, UserException.EmailNotFoundException::class) {
            coEvery {
                userRepository.validateAndMakeLogIn(any(), any())
            } throws UserException.EmailNotFoundException(EMAIL)
        }

    @Test
    fun `when email not validated then use case fails`() =
        runErrorTest(useCase, request, UserException.EmailNotValidatedException::class) {
            coEvery {
                userRepository.validateAndMakeLogIn(any(), any())
            } throws UserException.EmailNotValidatedException(EMAIL)
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery { userRepository.validateAndMakeLogIn(any(), any()) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.email, isEqualTo(EMAIL))
        }
}
