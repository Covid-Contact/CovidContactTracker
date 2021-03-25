package cat.covidcontact.tracker.util.handlers

import android.content.Context
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.util.extensions.showDialog

@Suppress("UNCHECKED_CAST")
class ScreenStateHandler<T : ScreenState>(
    var context: Context? = null,
    private val onLoading: (Context) -> Unit = {},
    private val onNoInternet: (Context) -> Unit = {
        it.showDialog(R.string.no_internet_title, R.string.no_internet_message)
    },
    private val onOtherError: (Context) -> Unit = {
        it.showDialog(R.string.other_error_title, R.string.other_error_message)
    },
    private val action: (Context, T) -> Unit
) {

    fun executeStateAction(state: ScreenState) {
        context?.let {
            when (state) {
                ScreenState.Loading -> onLoading(it)
                ScreenState.NoInternet -> onNoInternet(it)
                ScreenState.OtherError -> onOtherError(it)
                else -> action(it, state as T)
            }
        } ?: throw Exception("Please set context before using this handler")
    }
}