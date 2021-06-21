package cat.covidcontact.usecases.getContactNetworkByAccessCode

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
class GetContactNetworkByAccessCodeImplTest {
    private lateinit var useCase: GetContactNetworkByAccessCode

    @MockK
    private lateinit var contactNetworkRepository: ContactNetworkRepository

    private val code = "123456"
    private val request = GetContactNetworkByAccessCode.Request(code)

    @Before
    fun setUp() {
        contactNetworkRepository = mockk()
        coEvery {
            contactNetworkRepository.getContactNetworkByAccessCode(any())
        } returns contactNetwork

        useCase = GetContactNetworkByAccessCodeImpl(contactNetworkRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                contactNetworkRepository.getContactNetworkByAccessCode(any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery {
                contactNetworkRepository.getContactNetworkByAccessCode(any())
            } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.contactNetwork.name, isEqualTo(contactNetwork.name))
        }
}
