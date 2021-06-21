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
