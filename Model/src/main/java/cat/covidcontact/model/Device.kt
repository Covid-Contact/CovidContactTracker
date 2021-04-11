package cat.covidcontact.model

import cat.covidcontact.model.post.PostDevice

class Device(
    val id: String,
    val name: String? = null
) {

    fun createPost(): PostDevice {
        return PostDevice(id, name)
    }
}
