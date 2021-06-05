package cat.covidcontact.tracker.common.extensions

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.notify() {
    value = value
}
