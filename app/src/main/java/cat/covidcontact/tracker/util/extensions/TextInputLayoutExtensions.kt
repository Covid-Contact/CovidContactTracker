package cat.covidcontact.tracker.util.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(errorMsg: String?) {
    isErrorEnabled = errorMsg != null
    error = errorMsg
}

fun TextInputLayout.showErrorIf(isError: Boolean, errorMsg: String) {
    val msg = if (isError) errorMsg else null
    showError(msg)
}

fun TextInputLayout.isEmpty() = editText?.text?.isEmpty() ?: false
