package cat.covidcontact.data

interface UserRepository {
    fun makeLogIn(email: String, password: String): Any
}