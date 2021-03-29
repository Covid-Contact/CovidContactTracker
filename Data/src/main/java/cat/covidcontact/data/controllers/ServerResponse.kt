package cat.covidcontact.data.controllers

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result

data class ServerResponse(
    val request: Request,
    val response: Response,
    val result: Result<String, FuelError>
)