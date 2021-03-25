package cat.covidcontact.tracker.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.util.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {
    private val _screenState = MutableLiveData<ScreenState>(ScreenState.Loading)
    val screenState: LiveData<ScreenState>
        get() = _screenState

    protected fun loadState(state: ScreenState) {
        _screenState.value = state
    }

    protected suspend fun <I : UseCase.UseCaseRequest, O : UseCase.UseCaseResponse> executeUseCase(
        useCase: UseCase<I, O>,
        useCaseResultHandler: UseCaseResultHandler<O>,
        prepareInput: () -> I
    ) {
        val input = prepareInput()
        val result = withContext(Dispatchers.IO) {
            useCase.execute(input)
        }

        loadState(useCaseResultHandler.getScreenState(result))
    }
}