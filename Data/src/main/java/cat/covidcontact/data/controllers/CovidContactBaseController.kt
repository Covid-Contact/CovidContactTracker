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
            .responseString()

        return ServerResponse(request, response, result)
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
            .responseString()

        return ServerResponse(request, response, result)
    }

    suspend fun put(
        url: String,
        parameters: Parameters? = null,
        isAuthenticated: Boolean = true
    ): ServerResponse {
        val (request, response, result) = url.httpPut(parameters)
            .header(getAuthHeader(isAuthenticated))
            .responseString()

        return ServerResponse(request, response, result)
    }

    suspend fun delete(
        url: String,
        parameters: Parameters? = null,
        isAuthenticated: Boolean = true
    ): ServerResponse {
        val (request, response, result) = url.httpDelete(parameters)
            .header(getAuthHeader(isAuthenticated))
            .responseString()

        return ServerResponse(request, response, result)
    }

    private fun getAuthHeader(isAuthenticated: Boolean) = emptyMap<String, String>()

    companion object {
        const val NO_INTERNET = -1
        var token: String? = null
    }
}