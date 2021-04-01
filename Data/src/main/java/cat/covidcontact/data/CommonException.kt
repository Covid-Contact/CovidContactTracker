package cat.covidcontact.data

sealed class CommonException(msg: String) : RuntimeException(msg) {
    object NoInternetException : CommonException("There is not internet")
    object OtherError : CommonException("Unexpected error")
}