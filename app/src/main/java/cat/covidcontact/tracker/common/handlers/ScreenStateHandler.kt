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

import android.content.Context
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.extensions.cancelLoadingDialog
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.extensions.showLoadingDialog

@Suppress("UNCHECKED_CAST")
class ScreenStateHandler<T : ScreenState>(
    var context: Context? = null,
    private val onLoading: (Context) -> Unit = {},
    private val onNoInternet: (Context) -> Unit = {
        it.showDialog(R.string.no_internet_title, R.string.no_internet_message)
    },
    private val onOtherError: (Context) -> Unit = {
        it.showDialog(R.string.other_error_title, R.string.other_error_message)
    },
    private val onExecutingUseCase: (Context) -> Unit = { it.showLoadingDialog() },
    private val action: (Context, T) -> Unit
) {

    fun executeStateAction(state: ScreenState) {
        context?.let {
            cancelLoadingDialog()
            when (state) {
                ScreenState.Loading -> onLoading(it)
                ScreenState.NoInternet -> onNoInternet(it)
                ScreenState.OtherError -> onOtherError(it)
                ScreenState.ExecutingUseCase -> onExecutingUseCase(it)
                ScreenState.Nothing -> {
                }
                else -> action(it, state as T)
            }
        } ?: throw ContextNotSetException()
    }
}
