package cat.covidcontact.usecases.makelogout

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.*
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MakeLogOutImplTest {
    private lateinit var useCase: MakeLogOutImpl

    @MockK
    private lateinit var userRepository: UserRepository

    private val request = MakeLogOut.Request(EMAIL, DEVICE_ID)

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery { userRepository.makeLogOut(any(), any()) } returns Unit

        useCase = MakeLogOutImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                userRepository.makeLogOut(any(), any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery { userRepository.makeLogOut(any(), any()) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request)
}
