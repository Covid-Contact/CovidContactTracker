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

package cat.covidcontact.tracker.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.CombinedLiveData
import cat.covidcontact.tracker.common.handlers.InvalidFieldHandler
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

fun <T : ScreenState> LiveData<ScreenState>.observeScreenState(
    lifecycleOwner: LifecycleOwner,
    handler: ScreenStateHandler<T>
) {
    removeObservers(lifecycleOwner)
    observe(lifecycleOwner) {
        handler.executeStateAction(it)
    }
}

fun LiveData<Boolean>.observeInvalidField(
    lifecycleOwner: LifecycleOwner,
    textInputLayout: TextInputLayout,
    errorMessage: String,
    checkFunction: (String) -> Unit
) {
    val handler = InvalidFieldHandler(textInputLayout, checkFunction)
    observe(lifecycleOwner) { isInvalid ->
        handler.isBeingChecked = isInvalid
        textInputLayout.showErrorIf(isInvalid, errorMessage)
    }
}

fun LiveData<Boolean>.observerButtonEnabled(
    lifecycleOwner: LifecycleOwner,
    button: MaterialButton
) {
    observe(lifecycleOwner) { isEnabled ->
        button.isEnabled = isEnabled
    }
}

fun <T, K, S> LiveData<T>.combine(liveData: LiveData<K>, combine: (T?, K?) -> S) =
    CombinedLiveData(this, liveData, combine)

fun <T> LiveData<T>.requireValue(): T = value ?: throw Exception("LiveData value is not set")
