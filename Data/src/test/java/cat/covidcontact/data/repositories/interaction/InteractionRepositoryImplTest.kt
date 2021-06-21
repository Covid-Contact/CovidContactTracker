package cat.covidcontact.data.repositories.interaction

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.DEVICE_ID
import cat.covidcontact.data.EMAIL
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.data.controllers.interaction.InteractionController
import cat.covidcontact.data.setResponseCode
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InteractionRepositoryImplTest {
    private lateinit var interactionRepositoryImpl: InteractionRepositoryImpl

    @MockK
    private lateinit var interactionController: InteractionController

    @MockK
    private lateinit var request: Request

    @MockK
    private lateinit var response: Response

    @MockK
    private lateinit var result: Result<String, FuelError>

    private lateinit var serverResponse: ServerResponse

    @Before
    fun setUp() {
        request = mockk()
        response = mockk()
        result = mockk()
        serverResponse = ServerResponse(request, response, result)

        interactionController = mockk()
        setUpResponses()

        interactionRepositoryImpl = InteractionRepositoryImpl(interactionController)
    }

    private fun setUpResponses() {
        coEvery { interactionController.registerRead(any()) } returns serverResponse
        coEvery { interactionController.notifyPositive(any()) } returns serverResponse
    }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when registering reads there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            interactionRepositoryImpl.registerRead(
                currentDeviceId = DEVICE_ID,
                deviceIds = emptySet(),
                time = System.currentTimeMillis(),
                lat = null,
                lon = null
            )
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when registering reads there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            interactionRepositoryImpl.registerRead(
                currentDeviceId = DEVICE_ID,
                deviceIds = emptySet(),
                time = System.currentTimeMillis(),
                lat = null,
                lon = null
            )
        }

    @Test
    fun `when registering reads there is no error then it is registered`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.CREATED)
            interactionRepositoryImpl.registerRead(
                currentDeviceId = DEVICE_ID,
                deviceIds = emptySet(),
                time = System.currentTimeMillis(),
                lat = null,
                lon = null
            )
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when notifying positive there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            interactionRepositoryImpl.notifyPositive(EMAIL)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when notifying positive there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            interactionRepositoryImpl.notifyPositive(EMAIL)
        }

    @Test
    fun `when notifying positive there is no error then it is registered`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NO_CONTENT)
            interactionRepositoryImpl.notifyPositive(EMAIL)
        }

}
