package cat.covidcontact.model.post

import com.google.gson.annotations.SerializedName

data class PostToken(
    @SerializedName("email")
    val email: String,

    @SerializedName("token")
    val token: String
)
