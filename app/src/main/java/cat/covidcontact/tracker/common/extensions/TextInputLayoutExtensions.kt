package cat.covidcontact.tracker.common.extensions

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

fun TextInputLayout.showError(errorMsg: String?) {
    isErrorEnabled = errorMsg != null
    error = errorMsg
}

fun TextInputLayout.showErrorIf(isError: Boolean, errorMsg: String) {
    val msg = if (isError) errorMsg else null
    showError(msg)
}

fun TextInputLayout.isEmpty() = editText?.text?.isEmpty() ?: false

fun TextInputLayout.showDate(
    date: Long,
    format: String = "dd/MM/yyyy",
    locale: Locale = Locale.getDefault()
) {
    val formatter = SimpleDateFormat(format, locale)
    val calendar = Calendar.getInstance()
        .also {
            it.timeInMillis = date
        }
    editText?.setText(formatter.format(calendar.time))
}

fun TextInputLayout.runWhenTextChanged(action: () -> Unit) {
    editText?.addTextChangedListener(
        onTextChanged = { _, _, _, _ -> action() }
    )
}

fun TextInputLayout.addOnTextChanged(action: (CharSequence?) -> Unit) {
    editText?.addTextChangedListener(
        onTextChanged = { text, _, _, _ -> action(text) }
    )
}

fun TextInputLayout.makeRequired(isRequired: Boolean) {
    hint?.let {
        val endsWithAsterisk = it.endsWith("*")
        hint = when {
            isRequired && !endsWithAsterisk -> "$hint *"
            endsWithAsterisk -> it.dropLast(2)
            else -> it
        }
    }
}
