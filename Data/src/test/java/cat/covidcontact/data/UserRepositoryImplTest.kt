package cat.covidcontact.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl

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
