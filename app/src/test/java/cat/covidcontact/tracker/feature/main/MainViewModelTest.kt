package cat.covidcontact.tracker.feature.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.data.CommonException
import cat.covidcontact.data.user.UserException
import cat.covidcontact.model.user.Gender
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.MainCoroutineRule
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.getAfterLoading
import cat.covidcontact.usecases.UseCaseResult
import cat.covidcontact.usecases.userinfo.GetUserData
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    private lateinit var viewModel: MainViewModel

    private val email = "albert@gmail.com"
    private val username = email.substring(0, email.indexOf("@"))
    private val user = User(email, username, Gender.Male, System.currentTimeMillis())

    @MockK
    private lateinit var getUserData: GetUserData

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        getUserData = mockk()
        viewModel = MainViewModel(getUserData)
    }

    @Test
    fun `when there is no internet then the NoInternet state is loaded`() {
        coEvery { getUserData.execute(any()) } returns
            UseCaseResult.Error(CommonException.NoInternetException)

        viewModel.onGetCurrentUser(email)
        val state = viewModel.screenState.getAfterLoading()
        assertThat(state, instanceOf(ScreenState.NoInternet::class.java))
    }

    @Test
    fun `when there is an unexpected error then the OtherError state is loaded`() {
        coEvery { getUserData.execute(any()) } returns
            UseCaseResult.Error(CommonException.OtherError)

        viewModel.onGetCurrentUser(email)
        val state = viewModel.screenState.getAfterLoading()
        assertThat(state, instanceOf(ScreenState.OtherError::class.java))
    }

    @Test
    fun `when the user is new then the UserInfoNotFound state is loaded`() {
        coEvery { getUserData.execute(any()) } returns
            UseCaseResult.Error(UserException.UserInfoNotFound(email))

        viewModel.onGetCurrentUser(email)
        val state = viewModel.screenState.getAfterLoading()
        assertThat(state, instanceOf(MainState.UserInfoNotFound::class.java))

        val emailResult = (state as MainState.UserInfoNotFound).email
        assertThat(emailResult, `is`(email))
    }

    @Test
    fun `when the user is found in the system then the UserInfoFound state is loaded`() {
        coEvery { getUserData.execute(any()) } returns
            UseCaseResult.Success(GetUserData.Result(user))

        viewModel.onGetCurrentUser(email)
        val state = viewModel.screenState.getAfterLoading()
        assertThat(state, instanceOf(MainState.UserInfoFound::class.java))

        val userResult = (state as MainState.UserInfoFound).user
        assertThat(userResult, `is`(user))
    }
}
