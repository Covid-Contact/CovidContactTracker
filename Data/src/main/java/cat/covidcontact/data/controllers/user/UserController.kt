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

package cat.covidcontact.data.controllers.user

import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.ServerResponse
import cat.covidcontact.model.ApplicationUser
import cat.covidcontact.model.post.PostDevice
import cat.covidcontact.model.post.PostToken
import cat.covidcontact.model.post.PostUser

abstract class UserController : CovidContactBaseController() {
    override val url = "${super.url}/user"

    abstract suspend fun makeSignUp(applicationUser: ApplicationUser): ServerResponse
    abstract suspend fun isUserValidated(email: String): ServerResponse
    abstract suspend fun makeLogIn(applicationUser: ApplicationUser): ServerResponse
    abstract suspend fun getUserData(email: String): ServerResponse
    abstract suspend fun addUserData(user: PostUser): ServerResponse
    abstract suspend fun registerUserDevice(email: String, device: PostDevice): ServerResponse
    abstract suspend fun sendMessagingToken(token: PostToken): ServerResponse

    abstract suspend fun updateUserProfile(
        email: String,
        city: String,
        studies: String,
        occupation: String,
        marriage: String,
        children: Int,
        positive: Boolean?,
        vaccinated: Boolean?
    ): ServerResponse

    abstract suspend fun makeLogOut(email: String, deviceId: String): ServerResponse
    abstract suspend fun deleteAccount(email: String): ServerResponse
}
