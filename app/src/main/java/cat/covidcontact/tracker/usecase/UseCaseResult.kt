package cat.covidcontact.tracker.usecase

sealed class UseCaseResult<T> {
    class Success<T>(result: T) : UseCaseResult<T>()
    class Error(exception: Exception) : UseCaseResult<Nothing>()
}
