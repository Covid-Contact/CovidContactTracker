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

import android.app.Activity
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker

fun Fragment.hideKeyboard() {
    val activity = requireActivity()
    val view = activity.currentFocus ?: View(activity)
    val inputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

fun Fragment.showCalendarPicker(
    title: String,
    tag: String,
    selection: Long = MaterialDatePicker.todayInUtcMilliseconds(),
    inputMode: Int = MaterialDatePicker.INPUT_MODE_CALENDAR,
    onPositiveButtonClicked: (Long) -> Unit = {},
    onNegativeButtonClicked: (View) -> Unit = {},
    onCancel: (DialogInterface) -> Unit = {},
    onDismiss: (DialogInterface) -> Unit = {},
) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(title)
        .setSelection(selection)
        .setInputMode(inputMode)
        .build()
        .also {
            it.addOnPositiveButtonClickListener(onPositiveButtonClicked)
            it.addOnNegativeButtonClickListener(onNegativeButtonClicked)
            it.addOnCancelListener(onCancel)
            it.addOnDismissListener(onDismiss)
        }

    datePicker.show(requireActivity().supportFragmentManager, tag)
}

fun Fragment.showCalendarPicker(
    @StringRes title: Int,
    @StringRes tag: Int,
    selection: Long = MaterialDatePicker.todayInUtcMilliseconds(),
    inputMode: Int = MaterialDatePicker.INPUT_MODE_CALENDAR,
    onPositiveButtonClicked: (Long) -> Unit = {},
    onNegativeButtonClicked: (View) -> Unit = {},
    onCancel: (DialogInterface) -> Unit = {},
    onDismiss: (DialogInterface) -> Unit = {},
) {
    showCalendarPicker(
        getString(title),
        getString(tag),
        selection,
        inputMode,
        onPositiveButtonClicked,
        onNegativeButtonClicked,
        onCancel,
        onDismiss
    )
}