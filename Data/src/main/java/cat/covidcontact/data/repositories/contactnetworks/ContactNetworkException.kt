package cat.covidcontact.data.repositories.contactnetworks

sealed class ContactNetworkException(msg: String) : RuntimeException(msg) {
    object ContactNetworkAlreadyExisting : ContactNetworkException(
        "The contact network exists"
    )

    object ContactNetworkNotExisting : ContactNetworkException(
        "The contact network does not exist"
    )

    object InvalidAccessCode : ContactNetworkException(
        "The access code does not belong to any contact network"
    )
}
