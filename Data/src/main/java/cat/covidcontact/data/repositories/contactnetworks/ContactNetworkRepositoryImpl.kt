package cat.covidcontact.data.repositories.contactnetworks

import android.util.Log
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

        Log.i("Test", "createContactNetwork: ${serverResponse.request}")
        Log.i("Test", "createContactNetwork: ${serverResponse.response}")
        Log.i("Test", "createContactNetwork: ${serverResponse.result.get()}")

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
        Log.i("Test", "getContactNetworks: ${serverResponse.request}")
        Log.i("Test", "getContactNetworks: ${serverResponse.response}")
        Log.i("Test", "getContactNetworks: ${serverResponse.result.get()}")

        serverResponse.response.statusCode.run {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@run
                else -> throw CommonException.OtherError
            }
        }

        return Gson().fromJson(serverResponse.result.get())
    }
}
