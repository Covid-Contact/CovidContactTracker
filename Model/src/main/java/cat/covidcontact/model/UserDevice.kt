package cat.covidcontact.model

import cat.covidcontact.model.user.User

data class UserDevice(
    val user: User,
    val device: Device
)
