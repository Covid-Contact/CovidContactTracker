package cat.covidcontact.tracker.common.handlers

import android.content.Context
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.extensions.cancelLoadingDialog
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.extensions.showLoadingDialog

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
    private val onExecutingUseCase: (Context) -> Unit = { it.showLoadingDialog() },
    private val action: (Context, T) -> Unit
) {

    fun executeStateAction(state: ScreenState) {
        context?.let {
            it.cancelLoadingDialog()
            when (state) {
                ScreenState.Loading -> onLoading(it)
                ScreenState.NoInternet -> onNoInternet(it)
                ScreenState.OtherError -> onOtherError(it)
                ScreenState.ExecutingUseCase -> onExecutingUseCase(it)
                else -> action(it, state as T)
            }
        } ?: throw Exception("Please do not forget to call super.onCreateView()")
    }
}