package cat.covidcontact.model.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User(
    @SerializedName("email")
    val email: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("gender")
    val gender: Gender,

    @SerializedName("birth_date")
    val birthDate: Long,

    @SerializedName("city")
    var city: String? = null,

    @SerializedName("studies")
    var studies: String? = null,

    @SerializedName("occupation")
    var occupation: Occupation? = null,

    @SerializedName("marriage")
    var marriage: Marriage? = null,

    @SerializedName("children")
    var children: Int? = null,

    @SerializedName("has_been_positive")
    var hasBeenPositive: Boolean? = null,

    @SerializedName("is_vaccinated")
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