package cat.covidcontact.usecases.sendmessagingtoken

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.EMAIL
import cat.covidcontact.usecases.runNoInternetTest
import cat.covidcontact.usecases.runOtherErrorTest
import cat.covidcontact.usecases.runSuccessTest
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SendMessagingTokenImplTest {
    private lateinit var useCase: SendMessagingTokenImpl

    @MockK
    private lateinit var userRepository: UserRepository

    private val request = SendMessagingToken.Request(EMAIL)

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery { userRepository.sendMessagingToken(any()) } returns Unit

        useCase = SendMessagingTokenImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                userRepository.sendMessagingToken(any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery { userRepository.sendMessagingToken(any()) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request)
}
