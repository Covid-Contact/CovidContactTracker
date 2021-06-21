package cat.covidcontact.usecases.notifypositive

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.interaction.InteractionRepository
import cat.covidcontact.usecases.*
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NotifyPositiveImplTest {
    private lateinit var useCase: NotifyPositiveImpl

    @MockK
    private lateinit var interactionRepository: InteractionRepository

    private val request = NotifyPositive.Request(user)

    @Before
    fun setUp() {
        interactionRepository = mockk()
        coEvery { interactionRepository.notifyPositive(any()) } returns Unit

        useCase = NotifyPositiveImpl(interactionRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                interactionRepository.notifyPositive(any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery { interactionRepository.notifyPositive(any()) } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.user, isEqualTo(user))
        }
}
