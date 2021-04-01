package cat.covidcontact.tracker.common.handlers

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

class InvalidFieldHandler(
    textInputLayout: TextInputLayout,
    checkFunction: (String) -> Unit
) {
    var isBeingChecked = false

    init {
        textInputLayout.editText?.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                if (isBeingChecked) {
                    checkFunction(text.toString())
                }
            }
        )
    }
}