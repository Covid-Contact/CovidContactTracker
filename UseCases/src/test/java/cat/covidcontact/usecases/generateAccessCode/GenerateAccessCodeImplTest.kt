package cat.covidcontact.usecases.generateAccessCode

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.*
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GenerateAccessCodeImplTest {
    private lateinit var generateAccessCodeImpl: GenerateAccessCodeImpl

    @MockK
    private lateinit var contactNetworkRepository: ContactNetworkRepository

    private val code = "123456"
    private val request = GenerateAccessCode.Request(EMAIL, NAME)

    @Before
    fun setUp() {
        contactNetworkRepository = mockk()
        coEvery { contactNetworkRepository.generateAccessCode(any(), any()) } returns code

        generateAccessCodeImpl = GenerateAccessCodeImpl(contactNetworkRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(generateAccessCodeImpl, request) {
            coEvery {
                contactNetworkRepository.generateAccessCode(any(), any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(generateAccessCodeImpl, request) {
            coEvery {
                contactNetworkRepository.generateAccessCode(any(), any())
            } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(generateAccessCodeImpl, request) { response ->
            assertThat(response.accessCode, isEqualTo(code))
        }
}
