package cat.covidcontact.tracker.common.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.observeList(lifecycleOwner: LifecycleOwner, liveData: LiveData<List<T>>) {
    liveData.observe(lifecycleOwner) {
        (adapter as? ListAdapter<T, *>)?.submitList(it)
    }
}
