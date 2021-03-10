package cat.covidcontact.tracker.authactivity.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.tracker.MainCoroutineRule
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.authactivity.login.usecases.MakeLogIn
import cat.covidcontact.tracker.authactivity.model.Gender
import cat.covidcontact.tracker.authactivity.model.User
import cat.covidcontact.tracker.data.UserException
import cat.covidcontact.tracker.getAfterLoading
import cat.covidcontact.tracker.getOrAwaitValue
import cat.covidcontact.tracker.usecase.UseCaseResult
import cat.covidcontact.tracker.util.FieldValidator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class LogInViewModelTest {
    private lateinit var viewModel: LogInViewModel

    private val email = "albert@gmail.com"
    private val password = "Barcelona2020$"
    private val user = User(email, password, Gender.Male)

    @Mock
    private lateinit var makeLogIn: MakeLogIn

    @Mock
    private lateinit var fieldValidator: FieldValidator

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        makeLogIn = mock(MakeLogIn::class.java)

        fieldValidator = mock(FieldValidator::class.java)
        `when`(fieldValidator.isEmailValid(anyString())).thenReturn(true)
        `when`(fieldValidator.isPasswordValid(anyString())).thenReturn(true)

        viewModel = LogInViewModel(makeLogIn, fieldValidator)
    }

    @Test
    fun `change to sign up`() {
        // When the onChangeSignUp method is called
        viewModel.onChangeToSignUp()

        // Then the screen state is ChangeToSignUp
        assertThat(
            viewModel.screenState.getAfterLoading(),
            instanceOf(LogInState.ChangeToSignUp::class.java)
        )
    }

    @Test
    fun `anyEmptyField is false when the email is empty`() = runBlockingTest {
        // When the onMakeLogIn method is called with an empty email
        viewModel.onMakeLogIn("", password)

        // Then the anyEmptyField value is true
        assertThat(viewModel.anyEmptyField.value, `is`(true))
    }

    @Test
    fun `isEmailInvalid is true when the email is invalid`() = runBlockingTest {
        // Given a field validator that verifies the invalidity of the email
        `when`(fieldValidator.isEmailValid(anyString())).thenReturn(false)

        // When the onMakeLogIn method is called
        viewModel.onMakeLogIn(email, password)

        // Then the isEmailInvalid value is true
        assertThat(viewModel.isEmailInvalid.value, `is`(true))
    }

    @Test
    fun `anyEmptyField and isEmailInvalid are false if email is correct`() = runBlockingTest {
        // When the onMakeLogIn method is called
        viewModel.onMakeLogIn(email, password)

        // Then the anyEmptyField and isEmailInvalid values are false
        assertThat(viewModel.anyEmptyField.value, `is`(false))
        assertThat(viewModel.isEmailInvalid.value, `is`(false))
    }

    @Test
    fun `anyEmptyField is false when the password is empty`() = runBlockingTest {
        // When the onMakeLogIn method is called with an empty password
        viewModel.onMakeLogIn(email, "")

        // Then the anyEmptyField value is true
        assertThat(viewModel.anyEmptyField.value, `is`(true))
    }

    @Test
    fun `isPasswordInvalid is true when the password is invalid`() = runBlockingTest {
        // Given a field validator that verifies the invalidity of the password
        `when`(fieldValidator.isPasswordValid(anyString())).thenReturn(false)

        // When the onMakeLogIn method is called
        viewModel.onMakeLogIn(email, password)

        // Then the isPasswordInvalid value is true
        assertThat(viewModel.isPasswordInvalid.value, `is`(true))
    }

    @Test
    fun `anyEmptyField and isPasswordInvalid are false if password is correct`() = runBlockingTest {
        // When the onMakeLogIn method is called
        viewModel.onMakeLogIn(email, password)

        // Then the anyEmptyField and isPasswordInvalid values are false
        assertThat(viewModel.anyEmptyField.value, `is`(false))
        assertThat(viewModel.isPasswordInvalid.value, `is`(false))
    }

    @Test
    fun `when the viewModel is initiated then the screen state is Loading`() = runBlockingTest {
        assertThat(viewModel.screenState.getOrAwaitValue(), `is`(ScreenState.Loading))
    }

    @Test
    fun `makeLogIn use case is executed`() {
        // When the onMakeLogIn method is called
        viewModel.onMakeLogIn(email, password)

        // Then the use case is executed
        verify(makeLogIn).execute(MakeLogIn.Request(email, password))
    }

    @Test
    fun `no internet makeLogIn use case fails`() = runBlockingTest {
        // When there is no internet
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Error(UserException.NoInternetException)
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is NoInternet
        assertThat(
            viewModel.screenState.getAfterLoading(),
            instanceOf(ScreenState.NoInternet::class.java)
        )
    }

    @Test
    fun `other error makeLogIn use case fails`() = runBlockingTest {
        // When there is an other error
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Error(UserException.OtherError("Other"))
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is OtherError
        assertThat(
            viewModel.screenState.getAfterLoading(),
            instanceOf(ScreenState.OtherError::class.java)
        )
    }

    @Test
    fun `email not found makeLogIn use case fails`() = runBlockingTest {
        // When there is an other error
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Error(UserException.EmailNotFoundException(email))
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is EmailNotFound
        val state = viewModel.screenState.getAfterLoading()
        assertThat(
            state,
            instanceOf(LogInState.EmailNotFound(email)::class.java)
        )

        val emailNotFound = state as LogInState.EmailNotFound
        assertThat(emailNotFound.email, `is`(email))
    }

    @Test
    fun `wrong password makeLogIn use case fails`() = runBlockingTest {
        // When there is an other error
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Error(UserException.WrongPasswordException)
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is WrongPassword
        assertThat(
            viewModel.screenState.getAfterLoading(),
            instanceOf(LogInState.WrongPassword::class.java)
        )
    }

    @Test
    fun `email not validated makeLogIn use case fails`() = runBlockingTest {
        // When there is an other error
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Error(UserException.EmailNotValidatedException(email))
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is EmailNotValidated
        val state = viewModel.screenState.getAfterLoading()
        assertThat(
            state,
            instanceOf(LogInState.EmailNotValidated(email)::class.java)
        )

        val emailNotValidated = state as LogInState.EmailNotValidated
        assertThat(emailNotValidated.email, `is`(email))
    }

    @Test
    fun `email valid and correct password makeLogIn use case success`() = runBlockingTest {
        // When there is an other error
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Success(MakeLogIn.Response(user))
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is SuccessLogIn and has the email
        val state = viewModel.screenState.getAfterLoading()
        assertThat(
            state,
            instanceOf(LogInState.SuccessLogIn::class.java)
        )

        val successLogInState = state as LogInState.SuccessLogIn
        assertThat(successLogInState.user, `is`(user))
    }
}
