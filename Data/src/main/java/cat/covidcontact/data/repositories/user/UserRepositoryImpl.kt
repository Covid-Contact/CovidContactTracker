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

import cat.covidcontact.data.CommonException
import cat.covidcontact.data.controllers.CovidContactBaseController
import cat.covidcontact.data.controllers.HttpStatus
import cat.covidcontact.data.controllers.user.UserController
import cat.covidcontact.model.ApplicationUser
import cat.covidcontact.model.Device
import cat.covidcontact.model.post.PostToken
import cat.covidcontact.model.post.PostUser
import cat.covidcontact.model.user.User
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val userController: UserController,
    private val firebaseMessaging: FirebaseMessaging
) : UserRepository {

    override suspend fun validateAndMakeLogIn(email: String, password: String) {
        val serverResponse = userController.isUserValidated(email)
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@onStatusCode
                HttpStatus.NOT_FOUND -> throw UserException.EmailNotFoundException(email)
                HttpStatus.BAD_REQUEST -> throw UserException.EmailNotValidatedException(email)
                else -> throw CommonException.OtherError
            }
        }

        if (!serverResponse.result.get().toBoolean()) {
            throw UserException.EmailNotValidatedException(email)
        }

        makeLogInRequest(email, password)
    }

    suspend fun makeLogInRequest(email: String, password: String) {
        val serverResponse = userController.makeLogIn(ApplicationUser(email, password))
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@onStatusCode
                HttpStatus.FORBIDDEN -> throw UserException.WrongPasswordException
                HttpStatus.NOT_FOUND -> throw UserException.EmailNotFoundException(email)
                else -> throw CommonException.OtherError
            }
        }

        val token = serverResponse.response[CovidContactBaseController.AUTH_HEADER].first()
            .toString()
        CovidContactBaseController.token = token
    }

    override suspend fun makeSignUp(email: String, password: String) {
        val serverResponse = userController.makeSignUp(ApplicationUser(email, password))
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@onStatusCode
                HttpStatus.BAD_REQUEST -> throw UserException.EmailAlreadyRegistered(email)
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun getUserData(email: String): User {
        val serverResponse = userController.getUserData(email)
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.OK -> return@onStatusCode
                HttpStatus.NOT_FOUND -> throw UserException.UserInfoNotFound(email)
                else -> throw CommonException.OtherError
            }
        }

        val postUser = Gson().fromJson(serverResponse.result.get(), PostUser::class.java)
        return User.fromPost(postUser)
    }

    override suspend fun addUserData(user: User): String {
        val serverResponse = userController.addUserData(user.createPost())
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@onStatusCode
                HttpStatus.BAD_REQUEST -> throw UserException.UserInfoFound(user.email)
                else -> throw CommonException.OtherError
            }
        }

        return user.email
    }

    override suspend fun registerUserDevice(email: String, device: Device) {
        val serverResponse = userController.registerUserDevice(email, device.createPost())
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@onStatusCode
                HttpStatus.NOT_FOUND -> throw UserException.EmailNotFoundException(email)
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun sendMessagingToken(email: String) {
        val token = getMessagingToken()
        val serverResponse = userController.sendMessagingToken(PostToken(email, token))
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.CREATED -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun updateUserData(
        email: String,
        city: String,
        studies: String,
        occupation: String,
        marriage: String,
        children: Int,
        positive: Boolean?,
        vaccinated: Boolean?
    ) {
        val serverResponse = userController.updateUserProfile(
            email,
            city,
            studies,
            occupation,
            marriage,
            children,
            positive,
            vaccinated
        )

        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun makeLogOut(email: String, device: String) {
        val serverResponse = userController.makeLogOut(email, device)
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }
    }

    override suspend fun deleteAccount(email: String) {
        val serverResponse = userController.deleteAccount(email)
        serverResponse.onStatusCode {
            when (this) {
                CovidContactBaseController.NO_INTERNET -> throw CommonException.NoInternetException
                HttpStatus.NO_CONTENT -> return@onStatusCode
                else -> throw CommonException.OtherError
            }
        }
    }

    private suspend fun getMessagingToken(): String {
        val task = firebaseMessaging.token
        task.await()

        if (!task.isSuccessful) {
            throw task.exception ?: CommonException.OtherError
        }

        return task.result!!
    }
}
