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

package cat.covidcontact.model.post

import cat.covidcontact.model.user.Gender
import cat.covidcontact.model.user.Marriage
import cat.covidcontact.model.user.Occupation
import cat.covidcontact.model.user.UserState
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PostUser(
    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("gender")
    val gender: Gender,

    @SerializedName("birth_date")
    val birthDate: Long,

    @SerializedName("city")
    var city: String? = null,

    @SerializedName("studies")
    var studies: String? = null,

    @SerializedName("occupation")
    var occupation: Occupation? = null,

    @SerializedName("marriage")
    var marriage: Marriage? = null,

    @SerializedName("children")
    var children: Int? = null,

    @SerializedName("has_been_positive")
    var hasBeenPositive: Boolean? = null,

    @SerializedName("vaccinated")
    var isVaccinated: Boolean? = null,

    @SerializedName("state")
    var state: UserState? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostUser

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

    override fun toString() =
        "User(email='$email', " +
            "username='$username', " +
            "gender=$gender, " +
            "birthDate=$birthDate, " +
            "city=$city, " +
            "studies=$studies, " +
            "occupation=$occupation, " +
            "marriage=$marriage, " +
            "children=$children, " +
            "hasBeenPositive=$hasBeenPositive, " +
            "isVaccinated=$isVaccinated)"

}
