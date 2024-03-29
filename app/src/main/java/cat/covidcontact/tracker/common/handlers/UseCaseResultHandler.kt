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
