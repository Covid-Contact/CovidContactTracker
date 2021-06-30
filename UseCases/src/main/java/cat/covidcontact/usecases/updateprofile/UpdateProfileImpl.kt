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

package cat.covidcontact.usecases.updateprofile

import cat.covidcontact.data.repositories.user.UserRepository
import cat.covidcontact.model.user.Marriage
import cat.covidcontact.model.user.Occupation
import cat.covidcontact.usecases.runUseCase

class UpdateProfileImpl(
    private val userRepository: UserRepository
) : UpdateProfile {

    override suspend fun execute(request: UpdateProfile.Request) = runUseCase {
        userRepository.updateUserData(
            request.user.email,
            request.city,
            request.studies,
            request.occupation,
            request.marriage,
            request.children,
            request.positive,
            request.vaccinated,
        )

        request.user.apply {
            city = request.city
            studies = request.studies
            occupation = request.occupation.toOccupation()
            marriage = request.marriage.toMarriage()
            children = request.children
            hasBeenPositive = request.positive
            isVaccinated = request.vaccinated
        }
        UpdateProfile.Response()
    }

    private fun String.toOccupation(): Occupation? = try {
        Occupation.valueOf(this)
    } catch (e: Exception) {
        null
    }

    private fun String.toMarriage(): Marriage? = try {
        Marriage.valueOf(this)
    } catch (e: Exception) {
        null
    }
}
