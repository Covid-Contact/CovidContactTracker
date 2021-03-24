package cat.covidcontact.tracker.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.ScreenStateHandler

fun <T : ScreenState> LiveData<ScreenState>.observeScreenState(
    lifecycleOwner: LifecycleOwner,
    handler: ScreenStateHandler<T>
) {
    observe(lifecycleOwner) {
        handler.executeStateAction(it)
    }
}
