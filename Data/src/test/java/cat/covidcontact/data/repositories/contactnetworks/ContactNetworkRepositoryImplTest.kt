package cat.covidcontact.data.repositories.contactnetworks

import cat.covidcontact.data.*
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.data.controllers.contactnetwork.ContactNetworkController
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ContactNetworkRepositoryImplTest {
    private lateinit var contactNetworkRepositoryImpl: ContactNetworkRepositoryImpl

    @MockK
    private lateinit var contactNetworkController: ContactNetworkController

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

        contactNetworkController = mockk()
        setUpResponses()

        contactNetworkRepositoryImpl = ContactNetworkRepositoryImpl(contactNetworkController)
    }

    private fun setUpResponses() {
        coEvery {
            contactNetworkController.createContactNetwork(any())
        } returns serverResponse
        coEvery {
            contactNetworkController.getContactNetworks(any())
        } returns serverResponse
        coEvery {
            contactNetworkController.enableUserAddition(any(), any())
        } returns serverResponse
        coEvery {
            contactNetworkController.generateAccessCode(any(), any())
        } returns serverResponse
        coEvery {
            contactNetworkController.getContactNetworkByAccessCode(any())
        } returns serverResponse
        coEvery {
            contactNetworkController.joinContactNetwork(any(), any())
        } returns serverResponse
        coEvery {
            contactNetworkController.exitContactNetwork(any(), any())
        } returns serverResponse
        coEvery {
            contactNetworkController.deleteContactNetwork(any(), any())
        } returns serverResponse
    }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when creating contact network there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            contactNetworkRepositoryImpl.createContactNetwork(NAME, PASSWORD, user)
        }

    @Test(expected = ContactNetworkException.ContactNetworkAlreadyExisting::class)
    fun `when creating contact network it already exists fro user then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.BAD_REQUEST)
            contactNetworkRepositoryImpl.createContactNetwork(NAME, PASSWORD, user)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when creating contact network there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            contactNetworkRepositoryImpl.createContactNetwork(NAME, PASSWORD, user)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when getting contact networks there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            contactNetworkRepositoryImpl.getContactNetworks(EMAIL)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when getting contact networks there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            contactNetworkRepositoryImpl.getContactNetworks(EMAIL)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when enabling user addition there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            contactNetworkRepositoryImpl.enableUserAddition(NAME, true)
        }

    @Test(expected = ContactNetworkException.ContactNetworkNotExisting::class)
    fun `when enabling user addition contact network does not exist then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NOT_FOUND)
            contactNetworkRepositoryImpl.enableUserAddition(NAME, true)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when enabling user addition there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            contactNetworkRepositoryImpl.enableUserAddition(NAME, true)
        }

    @Test
    fun `when enabling user addition there is not any error then token is sent`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NO_CONTENT)
            contactNetworkRepositoryImpl.enableUserAddition(NAME, true)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when generating access code there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            contactNetworkRepositoryImpl.generateAccessCode(EMAIL, NAME)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when generating access code there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            contactNetworkRepositoryImpl.generateAccessCode(EMAIL, NAME)
        }

    @Test
    fun `when generating access code there is no error then it is returned`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.OK)
            setResultValue(result, ACCESS_CODE)

            val result = contactNetworkRepositoryImpl.generateAccessCode(EMAIL, NAME)
            assertThat(result, isEqualTo(ACCESS_CODE))
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when getting contact network by code there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            contactNetworkRepositoryImpl.getContactNetworkByAccessCode(ACCESS_CODE)
        }

    @Test(expected = ContactNetworkException.ContactNetworkNotExisting::class)
    fun `when getting contact network by code network does not exist then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NOT_FOUND)
            contactNetworkRepositoryImpl.getContactNetworkByAccessCode(ACCESS_CODE)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when getting contact network by code there is an error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            contactNetworkRepositoryImpl.getContactNetworkByAccessCode(ACCESS_CODE)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when joining contact network there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            contactNetworkRepositoryImpl.joinContactNetwork(EMAIL, NAME)
        }

    @Test(expected = ContactNetworkException.AlreadyJoined::class)
    fun `when joining contact network it is already joined then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.BAD_REQUEST)
            contactNetworkRepositoryImpl.joinContactNetwork(EMAIL, NAME)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when joining contact network there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            contactNetworkRepositoryImpl.joinContactNetwork(EMAIL, NAME)
        }

    @Test
    fun `when joining contact network there is not any error then it is joined`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NO_CONTENT)
            contactNetworkRepositoryImpl.joinContactNetwork(EMAIL, NAME)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when exiting contact network there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            contactNetworkRepositoryImpl.exitContactNetwork(EMAIL, NAME)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when exiting contact network there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            contactNetworkRepositoryImpl.exitContactNetwork(EMAIL, NAME)
        }

    @Test
    fun `when exiting contact network there is not any error then it is exited`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NO_CONTENT)
            contactNetworkRepositoryImpl.exitContactNetwork(EMAIL, NAME)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when deleting contact network there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            contactNetworkRepositoryImpl.exitContactNetwork(EMAIL, NAME)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when deleting contact network there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            contactNetworkRepositoryImpl.exitContactNetwork(EMAIL, NAME)
        }

    @Test
    fun `when deleting contact network there is not any error then it is exited`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NO_CONTENT)
            contactNetworkRepositoryImpl.exitContactNetwork(EMAIL, NAME)
        }
}
