package cat.covidcontact.tracker

import cat.covidcontact.data.UserException
import cat.covidcontact.usecases.UseCase
import cat.covidcontact.usecases.UseCaseResult

@Suppress("UNCHECKED_CAST")
class UseCaseResultHandler<T : UseCase.UseCaseResponse>(
    private val onSuccess: (T) -> ScreenState,
    private val onFailure: (Exception) -> ScreenState
) {

    fun getScreenState(useCaseResult: UseCaseResult<*>?): ScreenState {
        return when (useCaseResult) {
            null -> ScreenState.OtherError
            is UseCaseResult.Success -> onSuccess(useCaseResult.result as T)
            else -> {
                when (val exception = (useCaseResult as UseCaseResult.Error).exception) {
                    UserException.NoInternetException -> ScreenState.NoInternet
                    is UserException.OtherError -> ScreenState.OtherError
                    else -> onFailure(exception)
                }
            }
        }
    }
}
