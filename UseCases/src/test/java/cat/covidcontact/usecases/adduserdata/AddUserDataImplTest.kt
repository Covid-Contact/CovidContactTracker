package cat.covidcontact.usecases.adduserdata

import cat.covidcontact.data.CommonException
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
class AddUserDataImplTest {
    private lateinit var useCase: AddUserDataImpl

    @MockK
    private lateinit var userRepository: UserRepository

    private val request = AddUserData.Request(user)

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery { userRepository.addUserData(any()) } returns EMAIL

        useCase = AddUserDataImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                userRepository.addUserData(any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery { userRepository.addUserData(any()) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.email, isEqualTo(EMAIL))
        }
}
