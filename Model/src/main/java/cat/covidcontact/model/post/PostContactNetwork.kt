package cat.covidcontact.model.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostContactNetwork(
    @SerializedName("name")
    val name: String,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("owner_email")
    val ownerEmail: String? = null,

    @SerializedName("owner_username")
    val ownerUsername: String,

    @SerializedName("is_visible")
    val isVisible: Boolean = true,

    @SerializedName("is_password_protected")
    val isPasswordProtected: Boolean = true
) : Serializable
