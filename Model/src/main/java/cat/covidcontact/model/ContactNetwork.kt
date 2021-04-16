package cat.covidcontact.model

import cat.covidcontact.model.post.PostContactNetwork
import cat.covidcontact.model.user.User

class ContactNetwork(
    val name: String,
    val password: String? = null,
    val owner: User,
    val networkState: NetworkState = NetworkState.PositiveDetected
) {

    fun createPost(): PostContactNetwork {
        return PostContactNetwork(
            name = name,
            password = password,
            ownerEmail = owner.email,
            ownerUsername = owner.username
        )
    }

    override fun toString(): String {
        return "ContactNetwork(name='$name', " +
            "password=$password, " +
            "owner=$owner)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactNetwork

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromPost(postContactNetwork: PostContactNetwork): ContactNetwork {
            return with(postContactNetwork) {
                ContactNetwork(name, password, User(ownerUsername))
            }
        }
    }
}
