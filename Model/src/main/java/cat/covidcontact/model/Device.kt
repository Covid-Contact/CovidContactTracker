package cat.covidcontact.model

import java.security.MessageDigest

class Device(
    macAddress: String,
    val name: String? = null
) {
    val id = MessageDigest.getInstance("SHA-512")
        .digest(macAddress.toByteArray())
        .fold("") { str, byte -> str + "%02x".format(byte) }
}
