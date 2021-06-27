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

package cat.covidcontact.data

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import io.mockk.every
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher

fun setResponseCode(response: Response, statusCode: Int) {
    every { response.statusCode } returns statusCode
}

fun <T> setResultValue(result: Result<T, FuelError>, value: T) {
    every { result.get() } returns value
}

fun <T> isEqualTo(other: T): Matcher<T> = `is`(other)
