package cat.covidcontact.data.repositories.contactnetworks

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.contactnetwork.ContactNetworkController
import cat.covidcontact.data.fromJson
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.post.PostContactNetwork
import cat.covidcontact.model.user.User
import com.google.gson.Gson
import javax.inject.Inject

class ContactNetworkRepositoryImpl @Inject constructor(
    private val contactNetworkController: ContactNetworkController
) : ContactNetworkRepository {

    override suspend fun createContactNetwork(
        name: String,
        password: String?,
        owner: User
    ): ContactNetwork {
        val serverResponse = contactNetworkController.createContactNetwork(
            PostContactNetwork(
                name = name,
                password = password,
                ownerEmail = owner.email,
                ownerUsername = owner.username
            )
        )

        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@run
                HttpStatus.BAD_REQUEST ->
                    throw ContactNetworkException.ContactNetworkAlreadyExisting
                else -> throw CommonException.OtherError
            }
        }

        val postContactNetwork = Gson().fromJson(
            serverResponse.result.get(),
            PostContactNetwork::class.java
        )

        return ContactNetwork.fromPost(postContactNetwork)
    }

    override suspend fun getContactNetworks(email: String): List<ContactNetwork> {
        val serverResponse = contactNetworkController.getContactNetworks(email)

        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@run
                else -> throw CommonException.OtherError
            }
        }

        val contactNetworks = Gson().fromJson<List<PostContactNetwork>>(
            serverResponse.result.get()
        )
        return contactNetworks.map { ContactNetwork.fromPost(it) }
    }

    override suspend fun enableUserAddition(contactNetworkName: String, isEnabled: Boolean) {
        val serverResponse = contactNetworkController.enableUserAddition(
            contactNetworkName,
            isEnabled
        )

        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@run
                HttpStatus.NOT_FOUND -> throw ContactNetworkException.ContactNetworkNotExisting
                else -> throw CommonException.OtherError
            }
        }
    }
}
