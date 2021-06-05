package cat.covidcontact.tracker.common.extensions

import java.security.MessageDigest

fun String.generateDeviceId() = MessageDigest.getInstance("SHA-512")
    .digest(toByteArray())
    .fold("") { str, byte -> str + "%02x".format(byte) }
