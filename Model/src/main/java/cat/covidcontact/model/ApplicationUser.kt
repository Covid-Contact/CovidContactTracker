package cat.covidcontact.model

import java.io.Serializable

data class ApplicationUser(
    var email: String,
    var password: String
) : Serializable
