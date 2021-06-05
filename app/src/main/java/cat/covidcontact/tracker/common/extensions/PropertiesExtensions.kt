package cat.covidcontact.tracker.common.extensions

import android.view.View
import android.widget.Checkable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun View.observeEnabled(lifecycleOwner: LifecycleOwner, liveData: LiveData<Boolean>) {
    liveData.observe(lifecycleOwner) {
        isEnabled = it
    }
}

fun View.observeVisible(lifecycleOwner: LifecycleOwner, liveData: LiveData<Boolean>) {
    liveData.observe(lifecycleOwner) { isVisible ->
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}

fun Checkable.observeChecked(lifecycleOwner: LifecycleOwner, liveData: LiveData<Boolean>) {
    liveData.observe(lifecycleOwner) {
        isChecked = it
    }
}
