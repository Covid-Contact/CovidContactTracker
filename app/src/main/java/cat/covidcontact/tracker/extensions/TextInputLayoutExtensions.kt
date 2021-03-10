package cat.covidcontact.tracker.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.showError(errorMsg: String?) {
    isErrorEnabled = errorMsg != null
    error = errorMsg
}

fun TextInputLayout.isEmpty() = editText?.text?.isEmpty() ?: false
