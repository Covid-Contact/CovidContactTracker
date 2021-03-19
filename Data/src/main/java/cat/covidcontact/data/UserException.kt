package cat.covidcontact.data

abstract class UserException(msg: String) : RuntimeException(msg) {
    object NoInternetException : UserException("There is not internet")
    object WrongPasswordException : UserException("The password is wrong")
    class EmailNotFoundException(val email: String) : UserException("$email is not found")
    class EmailNotValidatedException(val email: String) : UserException("$email is not validated")
    class EmailAlreadyRegistered(val email: String) : UserException("$email already registered")
    class OtherError(msg: String) : UserException(msg)
}
