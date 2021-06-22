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

package cat.covidcontact.data.controllers

import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut

abstract class CovidContactBaseController {
    open val url = "http://covidcontact.cat:8080"

    suspend fun get(
        url: String,
        parameters: Parameters? = null,
        isAuthenticated: Boolean = true
    ): ServerResponse {
        val (request, response, result) = url.httpGet(parameters)
            .header(getAuthHeader(isAuthenticated))
            .timeout(TIMEOUT)
            .responseString()

        return ServerResponse(request, response, result).logConnectionResult()
    }

    suspend fun post(
        url: String,
        body: Any,
        parameters: Parameters? = null,
        isAuthenticated: Boolean = true
    ): ServerResponse {
        val (request, response, result) = url.httpPost(parameters)
            .jsonBody(body)
            .header(getAuthHeader(isAuthenticated))
            .timeout(TIMEOUT)
            .responseString()

        return ServerResponse(request, response, result).logConnectionResult()
    }

    suspend fun put(
        url: String,
        parameters: Parameters? = null,
        isAuthenticated: Boolean = true
    ): ServerResponse {
        val (request, response, result) = url.httpPut(parameters)
            .header(getAuthHeader(isAuthenticated))
            .timeout(TIMEOUT)
            .responseString()

        return ServerResponse(request, response, result).logConnectionResult()
    }

    suspend fun delete(
        url: String,
        parameters: Parameters? = null,
        isAuthenticated: Boolean = true
    ): ServerResponse {
        val (request, response, result) = url.httpDelete(parameters)
            .header(getAuthHeader(isAuthenticated))
            .timeout(TIMEOUT)
            .responseString()

        return ServerResponse(request, response, result).logConnectionResult()
    }

    private fun getAuthHeader(isAuthenticated: Boolean): Map<String, Any> {
        return token?.let {
            if (isAuthenticated) mapOf(AUTH_HEADER to it) else emptyMap()
        } ?: emptyMap()
    }

    companion object {
        const val NO_INTERNET = -1
        const val AUTH_HEADER = "Authorization"
        const val TIMEOUT = 60000
        var token: String? = null
    }
}
