package cat.covidcontact.usecases.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.data.UserException
import cat.covidcontact.data.UserRepository
import cat.covidcontact.usecases.MainCoroutineRule
import cat.covidcontact.usecases.UseCaseResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class MakeSignUpImplTest {
    private lateinit var makeSignUpImpl: MakeSignUpImpl

    private val email = "albert@gmail.com"
    private val password = "Barcelona2020$"

    @Mock
    private lateinit var userRepository: UserRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        userRepository = Mockito.mock(UserRepository::class.java)
        makeSignUpImpl = MakeSignUpImpl(userRepository)
    }

    @Test
    fun `no internet makes use case result error`() = runBlockingTest {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeSignUp(email, password))
            .thenThrow(UserException.NoInternetException)
        val result = makeSignUpImpl.execute(MakeSignUp.Request(email, password))
        verify(userRepository).makeSignUp(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val error = result as UseCaseResult.Error
        assertThat(
            error.exception,
            instanceOf(UserException.NoInternetException::class.java)
        )
    }

    @Test
    fun `other error makes use case result error`() = runBlockingTest {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeSignUp(email, password))
            .thenThrow(UserException.OtherError(""))
        val result = makeSignUpImpl.execute(MakeSignUp.Request(email, password))
        verify(userRepository).makeSignUp(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val error = result as UseCaseResult.Error
        assertThat(
            error.exception,
            instanceOf(UserException.OtherError::class.java)
        )
    }

    @Test
    fun `email already registered makes use case result error`() = runBlockingTest {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeSignUp(email, password))
            .thenThrow(UserException.EmailAlreadyRegistered(email))
        val result = makeSignUpImpl.execute(MakeSignUp.Request(email, password))
        verify(userRepository).makeSignUp(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val error = result as UseCaseResult.Error
        assertThat(
            error.exception,
            instanceOf(UserException.EmailAlreadyRegistered::class.java)
        )
    }

    @Test
    fun `use case success`() = runBlockingTest {
        // When there is not any exception thrown by the user repository
        val result = makeSignUpImpl.execute(MakeSignUp.Request(email, password))
        verify(userRepository).makeSignUp(email, password)

        // Then the use case success and the email is send as the result
        assertThat(result, instanceOf(UseCaseResult.Success::class.java))

        val success = result as UseCaseResult.Success
        assertThat(success.result.email, CoreMatchers.`is`(email))
    }
}
