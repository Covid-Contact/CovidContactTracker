package cat.covidcontact.tracker.common.extensions

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cat.covidcontact.tracker.R
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

inline fun <reified T : Enum<T>> TextInputLayout.setExposedMenuItems(
    context: Context,
    @LayoutRes layoutId: Int = R.layout.list_item
) {
    val values = enumValues<T>()
    val adapter = ArrayAdapter(context, layoutId, values)
    (editText as? AutoCompleteTextView)?.setAdapter(adapter)

    setOnFocusChangeListener { v, hasFocus ->
        if (hasFocus) {
            setText("")
        }
    }
}

fun TextInputLayout.setExposedMenuItems(
    context: Context,
    values: List<String>,
    @LayoutRes layoutId: Int = R.layout.list_item
) {
    val adapter = ArrayAdapter(context, layoutId, values)
    (editText as? AutoCompleteTextView)?.setAdapter(adapter)
}

fun TextInputLayout.setText(text: String) {
    editText?.setText(text)
}

fun TextInputLayout.setText(@StringRes textId: Int) {
    editText?.setText(textId)
}

fun TextInputLayout.observeText(liveData: MutableLiveData<String>) {
    editText?.addTextChangedListener(
        onTextChanged = { text, _, _, _ -> liveData.value = text.toString() }
    )
}

fun TextInputLayout.observeError(
    lifecycleOwner: LifecycleOwner,
    errorMsg: String,
    liveData: LiveData<Boolean>
) {
    liveData.observe(lifecycleOwner) {
        showErrorIf(it, errorMsg)
    }
}
