package cat.covidcontact.usecases.registerDevice

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
class RegisterDeviceImplTest {
    private lateinit var useCase: RegisterDeviceImpl

    @MockK
    private lateinit var userRepository: UserRepository

    private val request = RegisterDevice.Request(user, device)

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery { userRepository.registerUserDevice(any(), any()) } returns Unit

        useCase = RegisterDeviceImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                userRepository.registerUserDevice(any(), any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery { userRepository.registerUserDevice(any(), any()) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.userDevice.user, isEqualTo(user))
            assertThat(response.userDevice.device, isEqualTo(device))
        }
}
