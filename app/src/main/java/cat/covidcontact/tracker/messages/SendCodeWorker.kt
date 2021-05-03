package cat.covidcontact.tracker.messages

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessagesClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

@HiltWorker
class SendCodeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val messagesClient: MessagesClient
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        val message = Message(deviceId.toByteArray())

        repeat(3) {
            val task = messagesClient.publish(message)
            task.await()

            if (!task.isSuccessful) {
                task.exception?.printStackTrace()
                Result.failure()
            }

            delay(5000)
        }

        Result.success()
    }

    companion object {
        lateinit var deviceId: String
    }
}
