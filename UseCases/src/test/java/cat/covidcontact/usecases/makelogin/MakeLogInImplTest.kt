package cat.covidcontact.usecases.makelogin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.user.UserException
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.MainCoroutineRule
import cat.covidcontact.usecases.UseCaseResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class MakeLogInImplTest {
    private lateinit var makeLogInImpl: MakeLogInImpl

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
        userRepository = mock(UserRepository::class.java)
        makeLogInImpl = MakeLogInImpl(userRepository)
    }

    @Test
    fun `no internet makes use case result error`() = runBlockingTest {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeLogIn(email, password))
            .thenThrow(CommonException.NoInternetException)
        val result = makeLogInImpl.execute(MakeLogIn.Request(email, password))
        verify(userRepository).makeLogIn(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val error = result as UseCaseResult.Error
        assertThat(error.exception, instanceOf(CommonException.NoInternetException::class.java))
    }

    @Test
    fun `other error makes use case result error`() = runBlockingTest {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeLogIn(email, password))
            .thenThrow(CommonException.OtherError)
        val result = makeLogInImpl.execute(MakeLogIn.Request(email, password))
        verify(userRepository).makeLogIn(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val error = result as UseCaseResult.Error
        assertThat(error.exception, instanceOf(CommonException.OtherError::class.java))
    }

    @Test
    fun `wrong password makes use case result error`() = runBlockingTest {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeLogIn(email, password))
            .thenThrow(UserException.WrongPasswordException)
        val result = makeLogInImpl.execute(MakeLogIn.Request(email, password))
        verify(userRepository).makeLogIn(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val error = result as UseCaseResult.Error
        assertThat(error.exception, instanceOf(UserException.WrongPasswordException::class.java))
    }

    @Test
    fun `email not found makes use case result error`() = runBlockingTest {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeLogIn(email, password))
            .thenThrow(UserException.EmailNotFoundException(email))
        val result = makeLogInImpl.execute(MakeLogIn.Request(email, password))
        verify(userRepository).makeLogIn(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val error = result as UseCaseResult.Error
        assertThat(error.exception, instanceOf(UserException.EmailNotFoundException::class.java))
    }

    @Test
    fun `email not validated makes use case result error`() = runBlockingTest {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeLogIn(email, password))
            .thenThrow(UserException.EmailNotValidatedException(email))
        val result = makeLogInImpl.execute(MakeLogIn.Request(email, password))
        verify(userRepository).makeLogIn(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val error = result as UseCaseResult.Error
        assertThat(
            error.exception,
            instanceOf(UserException.EmailNotValidatedException::class.java)
        )
    }

    @Test
    fun `use case success`() = runBlockingTest {
        // When there is not any exception thrown by the user repository
        val result = makeLogInImpl.execute(MakeLogIn.Request(email, password))
        verify(userRepository).makeLogIn(email, password)

        // Then the use case success and the email is send as the result
        assertThat(result, instanceOf(UseCaseResult.Success::class.java))

        val success = result as UseCaseResult.Success
        assertThat(success.result.email, `is`(email))
    }
}
