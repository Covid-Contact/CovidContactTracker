package cat.covidcontact.data.services

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HttpRequest {
    suspend fun <T> make(call: Call<T>): HttpResponse<Response<T>, Throwable> {
        val channel = Channel<Int>()
        val httpResponse = HttpResponse<Response<T>, Throwable>()

        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>?, response: Response<T>) {
                httpResponse.success = response
                GlobalScope.launch { channel.send(0) }
            }

            override fun onFailure(call: Call<T>?, t: Throwable) {
                httpResponse.failure = t
                GlobalScope.launch { channel.send(0) }
            }
        })

        channel.receive()
        return httpResponse
    }

}