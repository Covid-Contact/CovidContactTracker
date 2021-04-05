package cat.covidcontact.usecases.getuserdata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.data.CommonException
import cat.covidcontact.data.user.UserException
import cat.covidcontact.data.user.UserRepository
import cat.covidcontact.model.user.Gender
import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.MainCoroutineRule
import cat.covidcontact.usecases.UseCaseResult
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetUserDataImplTest {
    private lateinit var getUserDataImpl: GetUserDataImpl

    private val email = "albert@gmail.com"
    private val username = email.substring(0, email.indexOf("@"))
    private val user = User(email, username, Gender.Male, System.currentTimeMillis())

    @MockK
    private lateinit var userRepository: UserRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        userRepository = mockk()
        getUserDataImpl = GetUserDataImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the result is an error`() = runBlockingTest {
        coEvery { userRepository.getUserData(email) } throws CommonException.NoInternetException
        val result = getUserDataImpl.execute(GetUserData.Request(email))

        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val exception = (result as UseCaseResult.Error).exception
        assertThat(exception, instanceOf(CommonException.NoInternetException::class.java))
    }

    @Test
    fun `when there is an other error then the result is an error`() = runBlockingTest {
        coEvery { userRepository.getUserData(email) } throws CommonException.OtherError
        val result = getUserDataImpl.execute(GetUserData.Request(email))

        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val exception = (result as UseCaseResult.Error).exception
        assertThat(exception, instanceOf(CommonException.OtherError::class.java))
    }

    @Test
    fun `when the user info is not found then the result is an error`() = runBlockingTest {
        coEvery { userRepository.getUserData(email) } throws UserException.UserInfoNotFound(email)
        val result = getUserDataImpl.execute(GetUserData.Request(email))

        assertThat(result, instanceOf(UseCaseResult.Error::class.java))

        val exception = (result as UseCaseResult.Error).exception
        assertThat(exception, instanceOf(UserException.UserInfoNotFound::class.java))

        val emailResult = (exception as UserException.UserInfoNotFound).email
        assertThat(emailResult, `is`(email))
    }

    @Test
    fun `when the user info is found then the result is a success`() = runBlockingTest {
        coEvery { userRepository.getUserData(email) } returns user
        val result = getUserDataImpl.execute(GetUserData.Request(email))

        assertThat(result, instanceOf(UseCaseResult.Success::class.java))

        val successResult = (result as UseCaseResult.Success).result
        assertThat(successResult.user, `is`(user))
    }
}
