package cat.covidcontact.tracker.messages

import com.google.firebase.messaging.FirebaseMessagingService

class ApplicationMessagingService : FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }
}
