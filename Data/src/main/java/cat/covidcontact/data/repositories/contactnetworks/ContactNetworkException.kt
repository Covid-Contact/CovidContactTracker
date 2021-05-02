package cat.covidcontact.data.repositories.contactnetworks

sealed class ContactNetworkException(msg: String) : RuntimeException(msg) {
    object ContactNetworkAlreadyExisting : ContactNetworkException(
        "The contact network exists"
    )

    object ContactNetworkNotExisting : ContactNetworkException(
        "The contact network does not exist"
    )

    class AlreadyJoined(val contactNetworkName: String) : ContactNetworkException(
        "The user has already joined $contactNetworkName"
    )
}
