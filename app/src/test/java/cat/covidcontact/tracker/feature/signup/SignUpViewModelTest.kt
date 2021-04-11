package cat.covidcontact.tracker.feature.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.user.UserException
import cat.covidcontact.tracker.MainCoroutineRule
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.fieldvalidator.FieldValidator
import cat.covidcontact.tracker.getAfterLoading
import cat.covidcontact.tracker.getOrAwaitValue
import cat.covidcontact.usecases.UseCaseResult
import cat.covidcontact.usecases.signup.MakeSignUp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class SignUpViewModelTest {
    private lateinit var viewModel: SignUpViewModel

    private val email = "albert@gmail.com"
    private val password = "Barcelona2020$"
    private val differentPassword = "Barcelona2020%"

    @Mock
    private lateinit var makeSignUp: MakeSignUp

    @Mock
    private lateinit var fieldValidator: FieldValidator

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        makeSignUp = mock(MakeSignUp::class.java)

        fieldValidator = mock(FieldValidator::class.java)
        `when`(fieldValidator.isEmailValid(anyString())).thenReturn(true)
        `when`(fieldValidator.isPasswordValid(anyString())).thenReturn(true)

        viewModel = SignUpViewModel(makeSignUp, fieldValidator)
    }

    @Test
    fun `change to log in`() {
        // When the onChangeToLogIn method is called
        viewModel.onChangeToLogIn()

        // Then the screen state is ChangeToLogIn
        assertThat(
            viewModel.screenState.getAfterLoading(),
            Matchers.instanceOf(SignUpState.ChangeToLogIn::class.java)
        )
    }

    @Test
    fun `isEmailInvalid is true when the email is invalid`() = runBlockingTest {
        // Given a field validator that verifies the invalidity of the email
        `when`(fieldValidator.isEmailValid(anyString())).thenReturn(false)

        // When the onMakeSignUp method is called
        viewModel.onMakeSignUp(email, password, password)

        // Then the isEmailInvalid value is true
        assertThat(viewModel.isEmailInvalid.value, `is`(true))
    }

    @Test
    fun `anyEmptyField and isEmailInvalid are false if email is correct`() = runBlockingTest {
        // When the onMakeSignUp method is called
        viewModel.onMakeSignUp(email, password, password)

        // Then the anyEmptyField and isEmailInvalid values are false
        assertThat(viewModel.isEmailInvalid.value, `is`(false))
    }

    @Test
    fun `isPasswordInvalid is true when the password is invalid`() = runBlockingTest {
        // Given a field validator that verifies the invalidity of the password
        `when`(fieldValidator.isPasswordValid(Mockito.anyString())).thenReturn(false)

        // When the onMakeSignUp method is called
        viewModel.onMakeSignUp(email, password, password)

        // Then the isPasswordInvalid value is true
        assertThat(viewModel.isPasswordInvalid.value, `is`(true))
    }

    @Test
    fun `arePasswordEquals is false when the passwords are not equals`() = runBlockingTest {
        // When the onMakeSignUp method is called with different password
        viewModel.onMakeSignUp(email, password, differentPassword)

        // Then the arePasswordEquals value is false
        assertThat(viewModel.isPasswordInvalid.value, `is`(false))
    }

    @Test
    fun `when the viewModel is initiated then the screen state is Loading`() = runBlockingTest {
        assertThat(viewModel.screenState.getOrAwaitValue(), `is`(ScreenState.Loading))
    }

    @Test
    fun `no internet makeLogIn use case fails`() = runBlockingTest {
        // When there is no internet
        `when`(makeSignUp.execute(MakeSignUp.Request(email, password))).thenReturn(
            UseCaseResult.Error(CommonException.NoInternetException)
        )

        viewModel.onMakeSignUp(email, password, password)

        // Then the screen state is NoInternet
        assertThat(
            viewModel.screenState.getAfterLoading(),
            Matchers.instanceOf(ScreenState.NoInternet::class.java)
        )
    }

    @Test
    fun `other error makeLogIn use case fails`() = runBlockingTest {
        // When there is an other error
        `when`(makeSignUp.execute(MakeSignUp.Request(email, password))).thenReturn(
            UseCaseResult.Error(CommonException.OtherError)
        )

        viewModel.onMakeSignUp(email, password, password)

        // Then the screen state is OtherError
        assertThat(
            viewModel.screenState.getAfterLoading(),
            Matchers.instanceOf(ScreenState.OtherError::class.java)
        )
    }

    @Test
    fun `email already registered makeSignUp use case fails`() = runBlockingTest {
        // When the email is already registered
        `when`(makeSignUp.execute(MakeSignUp.Request(email, password))).thenReturn(
            UseCaseResult.Error(UserException.EmailAlreadyRegistered(email))
        )

        viewModel.onMakeSignUp(email, password, password)

        // Then the screen state is EmailNotFound
        val state = viewModel.screenState.getAfterLoading()
        assertThat(
            state,
            Matchers.instanceOf(SignUpState.EmailAlreadyRegistered::class.java)
        )

        val emailAlreadyRegistered = state as SignUpState.EmailAlreadyRegistered
        assertThat(emailAlreadyRegistered.email, `is`(email))
    }

    @Test
    fun `email valid and correct password makeSignUp use case success`() = runBlockingTest {
        // When there is not any error
        `when`(makeSignUp.execute(MakeSignUp.Request(email, password))).thenReturn(
            UseCaseResult.Success(MakeSignUp.Response(email))
        )

        viewModel.onMakeSignUp(email, password, password)

        // Then the screen state is VerifyCodeSent and has the email
        val state = viewModel.screenState.getAfterLoading()
        assertThat(
            state,
            Matchers.instanceOf(SignUpState.VerifyEmailSent::class.java)
        )

        val verifyCodeSent = state as SignUpState.VerifyEmailSent
        assertThat(verifyCodeSent.email, `is`(email))
    }
}