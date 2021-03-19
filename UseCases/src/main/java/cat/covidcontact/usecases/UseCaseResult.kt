package cat.covidcontact.usecases

sealed class UseCaseResult<T> {
    class Success<T>(val result: T) : UseCaseResult<T>()
    class Error<T>(val exception: Exception) : UseCaseResult<T>()
}
