package cat.covidcontact.data.repositories.user

import cat.covidcontact.data.*
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.data.controllers.user.UserController
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @MockK
    private lateinit var userController: UserController

    @MockK
    private lateinit var request: Request

    @MockK
    private lateinit var response: Response

    @MockK
    private lateinit var result: Result<String, FuelError>

    @MockK
    private lateinit var firebaseMessaging: FirebaseMessaging

    @MockK
    private lateinit var tokenTask: Task<String>

    private lateinit var serverResponse: ServerResponse

    @Before
    fun setUp() {
        request = mockk()
        response = mockk()
        result = mockk()
        serverResponse = ServerResponse(request, response, result)

        every { response[any()] } returns listOf("")

        userController = mockk()
        setUpResponses()

        tokenTask = mockk()
        every { tokenTask.isComplete } returns true
        every { tokenTask.isSuccessful } returns true
        every { tokenTask.isCanceled } returns false
        every { tokenTask.exception } returns null
        every { tokenTask.result } returns ""

        firebaseMessaging = mockk()
        every { firebaseMessaging.token } returns tokenTask

        userRepositoryImpl = UserRepositoryImpl(userController, firebaseMessaging)
    }

    private fun setUpResponses() {
        coEvery { userController.makeSignUp(any()) } returns serverResponse
        coEvery { userController.isUserValidated(any()) } returns serverResponse
        coEvery { userController.makeLogIn(any()) } returns serverResponse
        coEvery { userController.getUserData(any()) } returns serverResponse
        coEvery { userController.addUserData(any()) } returns serverResponse
        coEvery { userController.registerUserDevice(any(), any()) } returns serverResponse
        coEvery { userController.sendMessagingToken(any()) } returns serverResponse

        coEvery {
            userController.updateUserProfile(any(), any(), any(), any(), any(), any(), any(), any())
        } returns serverResponse

        coEvery { userController.makeLogOut(any(), any()) } returns serverResponse
        coEvery { userController.deleteAccount(any()) } returns serverResponse
    }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when validate and making log in there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.validateAndMakeLogIn(EMAIL, PASSWORD)
        }

    @Test(expected = UserException.EmailNotFoundException::class)
    fun `when validate and making log in email is not found then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NOT_FOUND)
            userRepositoryImpl.validateAndMakeLogIn(EMAIL, PASSWORD)
        }

    @Test(expected = UserException.EmailNotValidatedException::class)
    fun `when validate and making log in email is not validated by url then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.BAD_REQUEST)
            userRepositoryImpl.validateAndMakeLogIn(EMAIL, PASSWORD)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when validate and making log in there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.validateAndMakeLogIn(EMAIL, PASSWORD)
        }

    @Test(expected = UserException.EmailNotValidatedException::class)
    fun `when validate and making log in email is not validated by result exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.OK)
            setResultValue(result, "false")
            userRepositoryImpl.validateAndMakeLogIn(EMAIL, PASSWORD)
        }

    @Test
    fun `when validate and making log in there is not any error then user is logged in`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.OK)
            setResultValue(result, "true")
            userRepositoryImpl.validateAndMakeLogIn(EMAIL, PASSWORD)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when making log in there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.makeLogInRequest(EMAIL, PASSWORD)
        }

    @Test(expected = UserException.EmailNotFoundException::class)
    fun `when making log in user is not found then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NOT_FOUND)
            userRepositoryImpl.makeLogInRequest(EMAIL, PASSWORD)
        }

    @Test(expected = UserException.WrongPasswordException::class)
    fun `when making log in user password is wrong then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.FORBIDDEN)
            userRepositoryImpl.makeLogInRequest(EMAIL, PASSWORD)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when making log in there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.makeLogInRequest(EMAIL, PASSWORD)
        }

    @Test
    fun `when making log in there is not any error then user is logged in`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.OK)
            userRepositoryImpl.makeLogInRequest(EMAIL, PASSWORD)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when making sign up there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.makeSignUp(EMAIL, PASSWORD)
        }

    @Test(expected = UserException.EmailAlreadyRegistered::class)
    fun `when making sign up email is already registered then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.BAD_REQUEST)
            userRepositoryImpl.makeSignUp(EMAIL, PASSWORD)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when making sign up there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.makeSignUp(EMAIL, PASSWORD)
        }

    @Test
    fun `when making sign up there is not any error then user is signed up`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.CREATED)
            userRepositoryImpl.makeSignUp(EMAIL, PASSWORD)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when getting user data there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.getUserData(EMAIL)
        }

    @Test(expected = UserException.UserInfoNotFound::class)
    fun `when getting user data email it is not found then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NOT_FOUND)
            userRepositoryImpl.getUserData(EMAIL)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when getting user data there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.getUserData(EMAIL)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when adding user data there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.addUserData(user)
        }

    @Test(expected = UserException.UserInfoFound::class)
    fun `when adding user data email it already exists then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.BAD_REQUEST)
            userRepositoryImpl.addUserData(user)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when adding user data there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.addUserData(user)
        }

    @Test
    fun `when adding user data there is not any error then user data is added`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.CREATED)

            val email = userRepositoryImpl.addUserData(user)
            assertThat(email, isEqualTo(EMAIL))
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when registering user device there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.registerUserDevice(EMAIL, device)
        }

    @Test(expected = UserException.EmailNotFoundException::class)
    fun `when registering user device email does not exist then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NOT_FOUND)
            userRepositoryImpl.registerUserDevice(EMAIL, device)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when registering user device there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.registerUserDevice(EMAIL, device)
        }

    @Test
    fun `when registering user device there is not any error then user data is added`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.CREATED)
            userRepositoryImpl.registerUserDevice(EMAIL, device)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when sending messaging token there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.sendMessagingToken(EMAIL)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when sending messaging token there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.sendMessagingToken(EMAIL)
        }

    @Test
    fun `when sending messaging token there is not any error then token is sent`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.CREATED)
            userRepositoryImpl.sendMessagingToken(EMAIL)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when  updating data here is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.updateUserData(
                EMAIL,
                CITY,
                STUDIES,
                OCCUPATION,
                MARRIAGE,
                CHILDREN,
                IS_POSITIVE,
                IS_VACCINATED
            )
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when updating data there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.updateUserData(
                EMAIL,
                CITY,
                STUDIES,
                OCCUPATION,
                MARRIAGE,
                CHILDREN,
                IS_POSITIVE,
                IS_VACCINATED
            )
        }

    @Test
    fun `when updating data there is not any error then data is updated`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NO_CONTENT)
            userRepositoryImpl.updateUserData(
                EMAIL,
                CITY,
                STUDIES,
                OCCUPATION,
                MARRIAGE,
                CHILDREN,
                IS_POSITIVE,
                IS_VACCINATED
            )
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when making log out there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.makeLogOut(EMAIL, DEVICE_ID)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when making log out there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.makeLogOut(EMAIL, DEVICE_ID)
        }

    @Test
    fun `when making log out there is not any error then token is sent`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NO_CONTENT)
            userRepositoryImpl.makeLogOut(EMAIL, DEVICE_ID)
        }

    @Test(expected = CommonException.NoInternetException::class)
    fun `when deleting account there is no internet then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, CovidContactBaseController.NO_INTERNET)
            userRepositoryImpl.deleteAccount(EMAIL)
        }

    @Test(expected = CommonException.OtherError::class)
    fun `when deleting account there is an unexpected error then exception is thrown`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.SERVER_ERROR)
            userRepositoryImpl.deleteAccount(EMAIL)
        }

    @Test
    fun `when deleting account there is not any error then token is sent`() =
        runBlockingTest {
            setResponseCode(response, HttpStatus.NO_CONTENT)
            userRepositoryImpl.deleteAccount(EMAIL)
        }
}
