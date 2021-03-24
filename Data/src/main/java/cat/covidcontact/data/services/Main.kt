package cat.covidcontact.data.services

import android.util.Log
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val url = "http://covidcontact.cat:8080/user/signup"
    /*val result = post(
        url = url,
        json = mapOf("email" to "apintogil@gmail.com", "password" to "Barcelona2020$")
    )*/

    val ep = UserService.EmailPassword("apintogil@gmail.com", "Barcelona2020$")
    val body = Gson().toJson(ep)

    val (request, response, result) = url.httpPost()
        .jsonBody(body)
        .responseString()
    Log.i("Test", "onViewCreated: $result")
}
