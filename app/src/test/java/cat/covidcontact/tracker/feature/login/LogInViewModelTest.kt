package cat.covidcontact.tracker.feature.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.data.CommonException
import cat.covidcontact.data.UserException
import cat.covidcontact.tracker.MainCoroutineRule
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.fieldvalidator.FieldValidator
import cat.covidcontact.tracker.getAfterLoading
import cat.covidcontact.tracker.getOrAwaitValue
import cat.covidcontact.usecases.UseCaseResult
import cat.covidcontact.usecases.login.MakeLogIn
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
        // When the onChangeToSignUp method is called
        viewModel.onChangeToSignUp()

        // Then the screen state is ChangeToSignUp
        assertThat(
            viewModel.screenState.getAfterLoading(),
            instanceOf(LogInState.ChangeToSignUp::class.java)
        )
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

        // Then the isEmailInvalid value is false
        assertThat(viewModel.isEmailInvalid.value, `is`(false))
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

        // Then the isPasswordInvalid value is false
        assertThat(viewModel.isPasswordInvalid.value, `is`(false))
    }

    @Test
    fun `when the viewModel is initiated then the screen state is Loading`() = runBlockingTest {
        assertThat(viewModel.screenState.getOrAwaitValue(), `is`(ScreenState.Loading))
    }

    @Test
    fun `no internet makeLogIn use case fails`() = runBlockingTest {
        // When there is no internet
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Error(CommonException.NoInternetException)
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
            UseCaseResult.Error(CommonException.OtherError)
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
        // When the email is not found
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Error(UserException.EmailNotFoundException(email))
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is EmailNotFound
        val state = viewModel.screenState.getAfterLoading()
        assertThat(
            state,
            instanceOf(LogInState.EmailNotFound::class.java)
        )

        val emailNotFound = state as LogInState.EmailNotFound
        assertThat(emailNotFound.email, `is`(email))
    }

    @Test
    fun `wrong password makeLogIn use case fails`() = runBlockingTest {
        // When the password is wrong
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
        // When the email is not validated
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Error(UserException.EmailNotValidatedException(email))
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is EmailNotValidated
        val state = viewModel.screenState.getAfterLoading()
        assertThat(
            state,
            instanceOf(LogInState.EmailNotValidated::class.java)
        )

        val emailNotValidated = state as LogInState.EmailNotValidated
        assertThat(emailNotValidated.email, `is`(email))
    }

    @Test
    fun `email valid and correct password makeLogIn use case success`() = runBlockingTest {
        // When there is not any error
        `when`(makeLogIn.execute(MakeLogIn.Request(email, password))).thenReturn(
            UseCaseResult.Success(MakeLogIn.Response(email))
        )

        viewModel.onMakeLogIn(email, password)

        // Then the screen state is SuccessLogIn and has the email
        val state = viewModel.screenState.getAfterLoading()
        assertThat(
            state,
            instanceOf(LogInState.SuccessLogIn::class.java)
        )

        val successLogInState = state as LogInState.SuccessLogIn
        assertThat(successLogInState.email, `is`(email))
    }
}
