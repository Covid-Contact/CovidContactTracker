package cat.covidcontact.usecases.enableUserAddition

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.repositories.contactnetworks.ContactNetworkRepository
import cat.covidcontact.usecases.contactNetwork
import cat.covidcontact.usecases.runNoInternetTest
import cat.covidcontact.usecases.runOtherErrorTest
import cat.covidcontact.usecases.runSuccessTest
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EnableUserAdditionImplTest {
    private lateinit var useCase: EnableUserAdditionImpl

    @MockK
    private lateinit var contactNetworkRepository: ContactNetworkRepository

    private val isEnabled = true
    private val request = EnableUserAddition.Request(contactNetwork, isEnabled)

    @Before
    fun setUp() {
        contactNetworkRepository = mockk()
        coEvery { contactNetworkRepository.enableUserAddition(any(), any()) } returns Unit

        useCase = EnableUserAdditionImpl(contactNetworkRepository)
    }

    @Test
    fun `when there is no internet then the use case fails`() =
        runNoInternetTest(useCase, request) {
            coEvery {
                contactNetworkRepository.enableUserAddition(any(), any())
            } throws CommonException.NoInternetException
        }

    @Test
    fun `when there is an unexpected error then the use case fails`() =
        runOtherErrorTest(useCase, request) {
            coEvery {
                contactNetworkRepository.enableUserAddition(any(), any())
            } throws Exception()
        }

    @Test
    fun `when there is not any error then the use case succeeds`() =
        runSuccessTest(useCase, request)
}
