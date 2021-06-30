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

package cat.covidcontact.usecases.signup

import cat.covidcontact.usecases.UseCase

interface MakeSignUp : UseCase<MakeSignUp.Request, MakeSignUp.Response> {
    data class Request(
        val email: String,
        val password: String
    ) : UseCase.UseCaseRequest

    data class Response(
        val email: String
    ) : UseCase.UseCaseResponse
}