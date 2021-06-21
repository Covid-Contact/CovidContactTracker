package cat.covidcontact.usecases.updateprofile

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.usecases.*
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateProfileImplTest {
    private lateinit var useCase: UpdateProfileImpl

    @MockK
    private lateinit var userRepository: UserRepository

    private val request = UpdateProfile.Request(
        email = EMAIL,
        city = CITY,
        studies = STUDIES,
        occupation = OCCUPATION,
        marriage = MARRIAGE,
        children = CHILDREN,
        positive = IS_POSITIVE,
        vaccinated = IS_VACCINATED
    )

    @Before
    fun setUp() {
        userRepository = mockk()
        coEvery {
            userRepository.updateUserData(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Unit

        useCase = UpdateProfileImpl(userRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                userRepository.updateUserData(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery {
                userRepository.updateUserData(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request)
}
