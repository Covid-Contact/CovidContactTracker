package cat.covidcontact.data

import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.Device
import cat.covidcontact.model.user.User

const val USERNAME = "test"
const val EMAIL = "test@gmail.com"
const val CITY = "Barcelona"
const val STUDIES = "University"
const val OCCUPATION = "Student"
const val MARRIAGE = "Single"
const val CHILDREN = 0
const val IS_POSITIVE = false
const val IS_VACCINATED = false
val user = User(username = USERNAME, email = EMAIL)

const val NAME = "testContactNetwork"
const val PASSWORD = "1234"
const val ACCESS_CODE = "123456"
val contactNetwork = ContactNetwork(name = NAME, password = PASSWORD, owner = user)

const val DEVICE_ID = "1234"
const val OTHER_DEVICE_ID = "5678"
const val DEVICE_NAME = "Device"
val device = Device(id = DEVICE_ID, name = DEVICE_NAME)
