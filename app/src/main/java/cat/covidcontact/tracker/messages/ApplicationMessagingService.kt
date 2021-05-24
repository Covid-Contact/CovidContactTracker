package cat.covidcontact.tracker.messages

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class ApplicationMessagingService : FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        Log.i("Read", "Token: $newToken")
    }
}
