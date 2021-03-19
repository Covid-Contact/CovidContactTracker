package cat.covidcontact.usecases.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.data.UserException
import cat.covidcontact.data.UserRepository
import cat.covidcontact.usecases.MainCoroutineRule
import cat.covidcontact.usecases.UseCaseResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `use case error`() {
        // When an exception is thrown by the user repository
        `when`(userRepository.makeLogIn(email, password))
            .thenThrow(UserException.OtherError(""))
        val result = makeLogInImpl.execute(MakeLogIn.Request(email, password))
        verify(userRepository).makeLogIn(email, password)

        // Then the use case returns an error
        assertThat(result, instanceOf(UseCaseResult.Error::class.java))
    }

    @Test
    fun `use case success`() {
        // When there is not any exception thrown by the user repository
        val result = makeLogInImpl.execute(MakeLogIn.Request(email, password))
        verify(userRepository).makeLogIn(email, password)

        // Then the use case success and the email is send as the result
        assertThat(result, instanceOf(UseCaseResult.Success::class.java))

        val success = result as UseCaseResult.Success
        assertThat(success.result.email, `is`(email))
    }
}
