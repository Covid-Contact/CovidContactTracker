package cat.covidcontact.tracker

import android.content.Context
import cat.covidcontact.tracker.extensions.showDialog

@Suppress("UNCHECKED_CAST")
class ScreenStateHandler<T : ScreenState>(
    var context: Context? = null,
    private val onLoading: () -> Unit = {},
    private val onNoInternet: () -> Unit = {
        context?.showDialog(R.string.no_internet_title, R.string.no_internet_message)
    },
    private val onOtherError: () -> Unit = {
        context?.showDialog(R.string.other_error_title, R.string.other_error_message)
    },
    private val action: (T) -> Unit
) {

    fun executeStateAction(state: ScreenState) {
        context?.let { Exception("Please set context before using this handler") }
        when (state) {
            ScreenState.Loading -> onLoading()
            ScreenState.NoInternet -> onNoInternet()
            ScreenState.OtherError -> onOtherError()
            else -> action(state as T)
        }
    }
}