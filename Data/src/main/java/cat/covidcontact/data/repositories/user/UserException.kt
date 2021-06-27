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

sealed class UserException(msg: String) : RuntimeException(msg) {
    object WrongPasswordException : UserException("The password is wrong")
    class EmailNotFoundException(val email: String) : UserException("$email is not found")
    class EmailNotValidatedException(val email: String) : UserException("$email is not validated")
    class EmailAlreadyRegistered(val email: String) : UserException("$email already registered")
    class UserInfoNotFound(val email: String) : UserException("$email info is not found")
    class UserInfoFound(val email: String) : UserException("$email info is found")
}
