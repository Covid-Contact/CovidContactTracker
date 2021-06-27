/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cat.covidcontact.tracker.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
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
        isExecutingUseCaseStateLoad: Boolean = true,
        prepareInput: () -> I
    ) {
        if (isExecutingUseCaseStateLoad) {
            loadState(ScreenState.ExecutingUseCase)
        }

        val input = prepareInput()
        val result = withContext(Dispatchers.IO) {
            useCase.execute(input)
        }

        loadState(useCaseResultHandler.getScreenState(result))
    }

    fun onLoadNothing() {
        loadState(ScreenState.Nothing)
    }
}
