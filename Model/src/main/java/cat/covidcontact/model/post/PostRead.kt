package cat.covidcontact.model.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostRead(
    @SerializedName("current_device_id")
    val currentDeviceId: String,

    @SerializedName("device_ids")
    val deviceIds: List<String>,

    @SerializedName("date_time")
    val dateTime: Long
) : Serializable
