package cat.covidcontact.data

sealed class UserException(msg: String) : RuntimeException(msg) {
    object WrongPasswordException : UserException("The password is wrong")
    class EmailNotFoundException(val email: String) : UserException("$email is not found")
    class EmailNotValidatedException(val email: String) : UserException("$email is not validated")
    class EmailAlreadyRegistered(val email: String) : UserException("$email already registered")
}
