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

package cat.covidcontact.data.repositories.user

import cat.covidcontact.model.Device
import cat.covidcontact.model.user.User

interface UserRepository {
    suspend fun validateAndMakeLogIn(email: String, password: String)
    suspend fun makeSignUp(email: String, password: String)
    suspend fun getUserData(email: String): User
    suspend fun addUserData(user: User): String
    suspend fun registerUserDevice(email: String, device: Device)
    suspend fun sendMessagingToken(email: String)

    suspend fun updateUserData(
        email: String,
        city: String,
        studies: String,
        occupation: String,
        marriage: String,
        children: Int,
        positive: Boolean?,
        vaccinated: Boolean?
    )

    suspend fun makeLogOut(email: String, device: String)
    suspend fun deleteAccount(email: String)
}
