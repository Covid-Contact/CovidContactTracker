package cat.covidcontact.usecases.sendread

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
class SendReadImplTest {
    private lateinit var useCase: SendReadImpl

    @MockK
    private lateinit var interactionRepository: InteractionRepository

    private val request = SendRead.Request(
        currentDeviceId = DEVICE_ID,
        deviceIds = setOf(OTHER_DEVICE_ID),
        time = System.currentTimeMillis(),
        lat = null,
        lon = null
    )

    private val emptyRequest = SendRead.Request(
        currentDeviceId = DEVICE_ID,
        deviceIds = setOf(),
        time = System.currentTimeMillis(),
        lat = null,
        lon = null
    )

    @Before
    fun setUp() {
        interactionRepository = mockk()
        coEvery {
            interactionRepository.registerRead(any(), any(), any(), any(), any())
        } returns Unit

        useCase = SendReadImpl(interactionRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                interactionRepository.registerRead(any(), any(), any(), any(), any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery {
                interactionRepository.registerRead(any(), any(), any(), any(), any())
            } throws Exception()
        }

    @Test
    fun `when there is not any error and is not empty then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.isEnded, isEqualTo(false))
        }

    @Test
    fun `when there is not any error and is empty then the use case succeeds`() =
        runSuccessTest(useCase, emptyRequest) { response ->
            assertThat(response.isEnded, isEqualTo(true))
        }
}
