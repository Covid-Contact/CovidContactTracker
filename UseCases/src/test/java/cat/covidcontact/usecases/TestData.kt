/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cat.covidcontact.usecases

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
val contactNetwork = ContactNetwork(name = NAME, password = PASSWORD, owner = user)

const val DEVICE_ID = "1234"
const val OTHER_DEVICE_ID = "5678"
const val DEVICE_NAME = "Device"
val device = Device(id = DEVICE_ID, name = DEVICE_NAME)
