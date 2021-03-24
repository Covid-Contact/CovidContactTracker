package cat.covidcontact.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cat.covidcontact.data.services.UserService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @Mock
    private lateinit var userService: UserService

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        /*userService = mock(UserService::class.java)
        userRepositoryImpl = UserRepositoryImpl(userService)*/
    }
}
