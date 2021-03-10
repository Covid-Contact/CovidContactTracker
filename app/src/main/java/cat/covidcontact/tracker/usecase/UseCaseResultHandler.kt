package cat.covidcontact.tracker.usecase

import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.data.UserException

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
