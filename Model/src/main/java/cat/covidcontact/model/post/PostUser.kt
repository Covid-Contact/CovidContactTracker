package cat.covidcontact.model.post

import cat.covidcontact.model.user.Gender
import cat.covidcontact.model.user.Marriage
import cat.covidcontact.model.user.Occupation
import cat.covidcontact.model.user.UserState
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PostUser(
    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

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

    @SerializedName("vaccinated")
    var isVaccinated: Boolean? = null,

    @SerializedName("state")
    var state: UserState? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostUser

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
