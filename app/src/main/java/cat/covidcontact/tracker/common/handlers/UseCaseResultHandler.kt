package cat.covidcontact.tracker.common.handlers

import cat.covidcontact.data.CommonException
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.usecases.UseCase
import cat.covidcontact.usecases.UseCaseResult

@Suppress("UNCHECKED_CAST")
class UseCaseResultHandler<T : UseCase.UseCaseResponse>(
    private val onSuccess: (T) -> ScreenState,
    private val onFailure: (Exception) -> ScreenState,
    private val isCommonEnabled: Boolean = true
) {

    fun getScreenState(useCaseResult: UseCaseResult<*>?): ScreenState {
        return when (useCaseResult) {
            null -> ScreenState.OtherError
            is UseCaseResult.Success -> onSuccess(useCaseResult.result as T)
            else -> {
                when (val exception = (useCaseResult as UseCaseResult.Error).exception) {
                    CommonException.OtherError -> if (isCommonEnabled) {
                        ScreenState.OtherError
                    } else {
                        ScreenState.Nothing
                    }
                    else -> onFailure(exception)
                }
            }
        }
    }
}
