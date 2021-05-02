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
