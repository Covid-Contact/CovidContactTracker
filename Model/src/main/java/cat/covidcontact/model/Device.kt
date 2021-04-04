package cat.covidcontact.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Device(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String? = null
) : Serializable
