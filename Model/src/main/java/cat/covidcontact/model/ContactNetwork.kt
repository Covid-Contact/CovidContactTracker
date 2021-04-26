package cat.covidcontact.model

import cat.covidcontact.model.post.PostContactNetwork
import cat.covidcontact.model.user.User
import java.io.Serializable

class ContactNetwork(
    val name: String,
    val password: String? = null,
    val owner: User,
    var isVisible: Boolean = false,
    val isPasswordProtected: Boolean = false,
    val accessCode: String? = null,
    val networkState: NetworkState = NetworkState.Normal
) : Serializable {

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
                ContactNetwork(
                    name = name,
                    password = password,
                    owner = User(ownerUsername),
                    isVisible = isVisible,
                    accessCode = accessCode,
                    isPasswordProtected = isPasswordProtected
                )
            }
        }
    }
}
