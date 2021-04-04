package cat.covidcontact.data.user

sealed class UserException(msg: String) : RuntimeException(msg) {
    object WrongPasswordException : UserException("The password is wrong")
    class EmailNotFoundException(val email: String) : UserException("$email is not found")
    class EmailNotValidatedException(val email: String) : UserException("$email is not validated")
    class EmailAlreadyRegistered(val email: String) : UserException("$email already registered")
    class UserInfoNotFound(val email: String) : UserException("$email info is not found")
    class UserInfoFound(val email: String) : UserException("$email info is found")
}
