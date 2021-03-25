package cat.covidcontact.tracker.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.util.InvalidFieldHandler
import cat.covidcontact.tracker.util.handlers.ScreenStateHandler
import com.google.android.material.textfield.TextInputLayout

fun <T : ScreenState> LiveData<ScreenState>.observeScreenState(
    lifecycleOwner: LifecycleOwner,
    handler: ScreenStateHandler<T>
) {
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
