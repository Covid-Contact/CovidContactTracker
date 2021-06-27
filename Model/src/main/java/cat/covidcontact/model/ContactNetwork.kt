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

package cat.covidcontact.model

import cat.covidcontact.model.post.PostContactNetwork
import cat.covidcontact.model.user.User
import java.io.Serializable

class ContactNetwork(
    val name: String,
    val password: String? = null,
    val owner: User,
    var isVisible: Boolean = false,
    val isPasswordProtected: Boolean = false,
    var accessCode: String? = null,
    var networkState: NetworkState = NetworkState.Normal
) : Serializable {

    fun createPost(): PostContactNetwork {
        return PostContactNetwork(
            name = name,
            password = password,
            ownerEmail = owner.email,
            ownerUsername = owner.username
        )
    }

    override fun toString(): String {
        return "ContactNetwork(name='$name', " +
            "password=$password, " +
            "owner=$owner)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactNetwork

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromPost(postContactNetwork: PostContactNetwork): ContactNetwork {
            return with(postContactNetwork) {
                ContactNetwork(
                    name = name,
                    password = password,
                    owner = User(ownerUsername),
                    isVisible = isVisible,
                    accessCode = accessCode,
                    isPasswordProtected = isPasswordProtected,
                    networkState = NetworkState.valueOf(state!!)
                )
            }
        }
    }
}
