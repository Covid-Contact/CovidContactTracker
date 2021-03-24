package cat.covidcontact.tracker.extensions

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.getStringWithParams(id: Int, paramsId: IntArray) = getString(id, paramsId)

fun Context.showDialog(
    title: String,
    message: String,
    positiveButtonText: String? = null,
    positiveButtonAction: (DialogInterface, Int) -> Unit = { _, _ -> },
    negativeButtonText: String? = null,
    negativeButtonAction: (DialogInterface, Int) -> Unit = { _, _ -> },
    neutralButtonText: String? = null,
    neutralButtonAction: (DialogInterface, Int) -> Unit = { _, _ -> },
    isCancelable: Boolean = true
) {
    val builder = MaterialAlertDialogBuilder(this)
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
    positiveButtonText: String? = null,
    positiveButtonAction: (DialogInterface, Int) -> Unit = { _, _ -> },
    negativeButtonText: String? = null,
    negativeButtonAction: (DialogInterface, Int) -> Unit = { _, _ -> },
    neutralButtonText: String? = null,
    neutralButtonAction: (DialogInterface, Int) -> Unit = { _, _ -> },
    isCancelable: Boolean = true
) {
    showDialog(
        getString(title),
        getString(message),
        positiveButtonText,
        positiveButtonAction,
        negativeButtonText,
        negativeButtonAction,
        neutralButtonText,
        neutralButtonAction,
        isCancelable
    )
}
