package cat.covidcontact.tracker.authactivity.model

class User(
    val email: String,
    val username: String,
    val gender: Gender,
    var studies: String? = null,
    var occupation: String? = null,
    var marriage: String? = null,
    var children: Int? = null
) {
    var authToken: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}