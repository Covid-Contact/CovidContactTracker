package cat.covidcontact.usecases.adduserdata

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.model.user.User
import cat.covidcontact.usecases.isEqualTo
import cat.covidcontact.usecases.runNoInternetTest
import cat.covidcontact.usecases.runOtherErrorTest
import cat.covidcontact.usecases.runSuccessTest
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddUserDataImplTest {
    private lateinit var addUserDataImpl: AddUserDataImpl

    @MockK
    private lateinit var userRepository: UserRepository

    private val username = "test"
    private val email = "test@gmail.com"
    private val user = User(username = "test", email = email)
    private val request = AddUserData.Request(user)

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery { userRepository.addUserData(any()) } returns email

        addUserDataImpl = AddUserDataImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(addUserDataImpl, request) {
            coEvery {
                userRepository.addUserData(any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(addUserDataImpl, request) {
            coEvery { userRepository.addUserData(any()) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(addUserDataImpl, request) { response ->
            assertThat(response.email, isEqualTo(email))
        }
}
