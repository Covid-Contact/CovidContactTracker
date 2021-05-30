package cat.covidcontact.data.controllers

import android.util.Log
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result

class ServerResponse(
    val request: Request,
    val response: Response,
    val result: Result<String, FuelError>
) {

    fun logConnectionResult(): ServerResponse {
        Log.i("Server", "Request: $request")
        Log.i("Server", "Response: $response")
        Log.i("Server", "Result: $result")

        return this
    }

    fun onStatusCode(action: Int.() -> Unit) {
        action(response.statusCode)
    }
}
