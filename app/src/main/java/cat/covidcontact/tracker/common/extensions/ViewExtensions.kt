package cat.covidcontact.tracker.common.extensions

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun View.observeEnabled(lifecycleOwner: LifecycleOwner, liveData: LiveData<Boolean>) {
    liveData.observe(lifecycleOwner) {
        isEnabled = it
    }
}
