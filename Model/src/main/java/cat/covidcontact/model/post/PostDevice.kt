package cat.covidcontact.model.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostDevice(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String? = null
) : Serializable
