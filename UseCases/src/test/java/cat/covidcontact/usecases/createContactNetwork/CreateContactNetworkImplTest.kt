package cat.covidcontact.usecases.createContactNetwork

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
class CreateContactNetworkImplTest {
    private lateinit var useCase: CreateContactNetworkImpl

    @MockK
    private lateinit var contactNetworkRepository: ContactNetworkRepository

    private val request = CreateContactNetwork.Request(NAME, PASSWORD, user)

    @Before
    fun setUp() {
        contactNetworkRepository = mockk()
        coEvery {
            contactNetworkRepository.createContactNetwork(any(), any(), any())
        } returns contactNetwork

        useCase = CreateContactNetworkImpl(contactNetworkRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                contactNetworkRepository.createContactNetwork(any(), any(), any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery {
                contactNetworkRepository.createContactNetwork(any(), any(), any())
            } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request) { response ->
            assertThat(response.contactNetwork, isEqualTo(contactNetwork))
        }
}
