package cat.covidcontact.data.services

class HttpResponse<S, F> {
    var success: S? = null
        set(value) {
            failure = null
            field = value
        }

    var failure: F? = null
        set(value) {
            success = null
            field = value
        }

    val isSuccessful: Boolean
        get() = success != null
}