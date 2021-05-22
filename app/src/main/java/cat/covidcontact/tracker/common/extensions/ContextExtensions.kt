package cat.covidcontact.tracker.common.extensions

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import cat.covidcontact.tracker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showDialog(
    title: String,
    message: String,
    positiveButtonText: String? = getString(android.R.string.ok),
    positiveButtonAction: (DialogInterface, Int) -> Unit = { dialog, _ -> dialog.dismiss() },
    negativeButtonText: String? = null,
    negativeButtonAction: (DialogInterface, Int) -> Unit = { dialog, _ -> dialog.dismiss() },
    neutralButtonText: String? = null,
    neutralButtonAction: (DialogInterface, Int) -> Unit = { dialog, _ -> dialog.dismiss() },
    isCancelable: Boolean = true
) {
    val builder = MaterialAlertDialogBuilder(this, R.style.dialog)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText, positiveButtonAction)
        .setNegativeButton(negativeButtonText, negativeButtonAction)
        .setNeutralButton(neutralButtonText, neutralButtonAction)
        .setCancelable(isCancelable)

    val dialog = builder.create()
    dialog.show()
}

fun Context.showDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes positiveButtonText: Int? = android.R.string.ok,
    positiveButtonAction: (DialogInterface, Int) -> Unit = { dialog, _ -> dialog.dismiss() },
    @StringRes negativeButtonText: Int? = null,
    negativeButtonAction: (DialogInterface, Int) -> Unit = { dialog, _ -> dialog.dismiss() },
    @StringRes neutralButtonText: Int? = null,
    neutralButtonAction: (DialogInterface, Int) -> Unit = { dialog, _ -> dialog.dismiss() },
    isCancelable: Boolean = true
) {
    showDialog(
        getString(title),
        getString(message),
        positiveButtonText?.let { positiveText -> getString(positiveText) },
        positiveButtonAction,
        negativeButtonText?.let { negativeText -> getString(negativeText) },
        negativeButtonAction,
        neutralButtonText?.let { neutralText -> getString(neutralText) },
        neutralButtonAction,
        isCancelable
    )
}

private var loadingDialog: Dialog? = null

fun Context.showLoadingDialog() {
    loadingDialog = Dialog(this)
    loadingDialog?.let {
        it.setContentView(R.layout.loading_dialog)
        it.setCancelable(false)
        it.show()
    }
}

fun cancelLoadingDialog() {
    loadingDialog?.let {
        if (it.isShowing) {
            it.dismiss()
        }
    }
}
