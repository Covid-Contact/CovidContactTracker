package cat.covidcontact.tracker.common.extensions

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cat.covidcontact.tracker.MainActivity
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

fun Context.showNotification(
    notificationId: Int,
    channelId: String,
    channelName: String,
    channelDescription: String,
    icon: Int,
    title: String,
    text: String
) {
    createNotificationChannel(channelId, channelName, channelDescription)

    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(icon)
        .setLargeIcon(BitmapFactory.decodeResource(resources, icon))
        .setContentTitle(title)
        .setContentText(text)
        .setStyle(NotificationCompat.BigTextStyle().bigText(text))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    NotificationManagerCompat.from(this).notify(notificationId, builder.build())
}

private fun Context.createNotificationChannel(
    channelId: String,
    channelName: String,
    channelDescription: String
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = channelDescription
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}
