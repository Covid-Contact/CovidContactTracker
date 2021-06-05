package cat.covidcontact.tracker.messages

import android.util.Log
import cat.covidcontact.model.user.UserState
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.extensions.showNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ApplicationMessagingService : FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Log.i("Message", "Token: $newToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i("Message", "onMessageReceived: ${message.data}")

        val state = UserState.valueOf(message.data[FIELD_STATE]!!)
        val (title, text) = when (state) {
            UserState.Quarantine ->
                R.string.notification_quarantine_title to R.string.notification_quarantine_text
            UserState.Prevention ->
                R.string.notification_prevention_title to R.string.notification_prevention_text
            else ->
                null to null
        }

        if (title != null && text != null) {
            applicationContext.showNotification(
                notificationId = NOTIFICATION_ID,
                channelId = CHANNEL_ID,
                channelName = CHANNEL_NAME,
                channelDescription = CHANNEL_DESCRIPTION,
                icon = R.mipmap.ic_launcher,
                title = applicationContext.getString(title),
                text = applicationContext.getString(text)
            )
        }
    }

    companion object {
        private const val FIELD_STATE = "State"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "Positive"
        private const val CHANNEL_NAME = "Positive detected"
        private const val CHANNEL_DESCRIPTION = "Positive has been detected"
    }
}
