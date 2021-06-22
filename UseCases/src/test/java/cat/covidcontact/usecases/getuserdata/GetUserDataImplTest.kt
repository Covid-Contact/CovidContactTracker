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

package cat.covidcontact.usecases.getuserdata

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
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
class GetUserDataImplTest {
    private lateinit var useCase: GetUserDataImpl

    private val request = GetUserData.Request(EMAIL)

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var contactNetworkRepository: ContactNetworkRepository

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery { userRepository.getUserData(EMAIL) } returns user

        contactNetworkRepository = mockk()
        coEvery { contactNetworkRepository.getContactNetworks(any()) } returns emptyList()

        useCase = GetUserDataImpl(userRepository, contactNetworkRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                userRepository.getUserData(EMAIL)
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when user info is not found then the use case fails`() =
        runErrorTest(useCase, request, UserException.UserInfoNotFound::class) {
            coEvery {
                userRepository.getUserData(EMAIL)
            } throws UserException.UserInfoNotFound(EMAIL)
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery { userRepository.getUserData(EMAIL) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.user, isEqualTo(user))
        }
}
