package cat.covidcontact.model

import java.io.Serializable

class User(
    val email: String,
    val username: String,
    val gender: Gender,
    val birthDate: Long,
    var city: String? = null,
    var studies: String? = null,
    var occupation: String? = null,
    var marriage: String? = null,
    var children: Int? = null,
    var hasBeenPositive: Boolean? = null,
    var isVaccinated: Boolean? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

    override fun toString() =
        "User(email='$email', " +
            "username='$username', " +
            "gender=$gender, " +
            "birthDate=$birthDate, " +
            "city=$city, " +
            "studies=$studies, " +
            "occupation=$occupation, " +
            "marriage=$marriage, " +
            "children=$children, " +
            "hasBeenPositive=$hasBeenPositive, " +
            "isVaccinated=$isVaccinated)"

}
