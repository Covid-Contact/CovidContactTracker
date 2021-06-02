package cat.covidcontact.model.user

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.post.PostUser

class User(
    val username: String,
    val email: String = "",
    val gender: Gender = Gender.Other,
    val birthDate: Long = 0,
    var city: String? = null,
    var studies: String? = null,
    var occupation: Occupation? = null,
    var marriage: Marriage? = null,
    var children: Int? = null,
    var hasBeenPositive: Boolean? = null,
    var isVaccinated: Boolean? = null,
    var state: UserState = UserState.Normal
) {
    private val _contactNetworks: MutableList<ContactNetwork> = mutableListOf()
    val contactNetworks: List<ContactNetwork>
        get() = _contactNetworks

    fun addContactNetwork(contactNetwork: ContactNetwork) {
        _contactNetworks.add(contactNetwork)
    }

    fun addContactNetworks(contactNetworks: List<ContactNetwork>) {
        contactNetworks.forEach { addContactNetwork(it) }
    }

    fun removeContactNetwork(contactNetwork: ContactNetwork) {
        _contactNetworks.remove(contactNetwork)
    }

    fun createPost(): PostUser {
        return PostUser(
            username,
            email,
            gender,
            birthDate,
            city,
            studies,
            occupation,
            marriage,
            children,
            hasBeenPositive,
            isVaccinated,
            state
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }

    override fun toString(): String {
        return "User(username='$username', " +
            "email='$email', " +
            "gender=$gender, " +
            "birthDate=$birthDate, " +
            "city=$city, " +
            "studies=$studies, " +
            "occupation=$occupation, " +
            "marriage=$marriage, " +
            "children=$children, " +
            "hasBeenPositive=$hasBeenPositive, " +
            "isVaccinated=$isVaccinated, " +
            "contactNetworks=$contactNetworks)"
    }

    companion object {
        @JvmStatic
        fun fromPost(postUser: PostUser): User {
            return with(postUser) {
                User(
                    username,
                    email,
                    gender,
                    birthDate,
                    city,
                    studies,
                    occupation,
                    marriage,
                    children,
                    hasBeenPositive,
                    isVaccinated,
                    state!!
                )
            }
        }
    }
}
