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
