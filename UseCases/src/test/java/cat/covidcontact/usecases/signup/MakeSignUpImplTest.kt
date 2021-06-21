package cat.covidcontact.usecases.signup

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
class MakeSignUpImplTest {
    private lateinit var useCase: MakeSignUpImpl

    @MockK
    private lateinit var userRepository: UserRepository

    private val request = MakeSignUp.Request(EMAIL, PASSWORD)

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery { userRepository.makeSignUp(any(), any()) } returns Unit

        useCase = MakeSignUpImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                userRepository.makeSignUp(any(), any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when email already registered then use case fails`() =
        runErrorTest(useCase, request, UserException.EmailAlreadyRegistered::class) {
            coEvery {
                userRepository.makeSignUp(any(), any())
            } throws UserException.EmailAlreadyRegistered(EMAIL)
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery { userRepository.makeSignUp(any(), any()) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.email, isEqualTo(EMAIL))
        }
}
