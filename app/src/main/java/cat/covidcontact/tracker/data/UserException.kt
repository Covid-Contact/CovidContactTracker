package cat.covidcontact.tracker.data

abstract class UserException(msg: String) : Exception(msg) {
    object NoInternetException : UserException("There is not internet")
    object WrongPasswordException : UserException("The password is wrong")
    class OtherError(msg: String) : UserException(msg)
    class EmailNotFoundException(val email: String) : UserException("$email is not found")
    class EmailNotValidatedException(val email: String) : UserException("$email is not validated")
}
