package cat.covidcontact.tracker.messages

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessagesClient
import com.google.android.gms.nearby.messages.PublishOptions
import com.google.android.gms.nearby.messages.Strategy
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

@HiltWorker
class PublishCodeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val messagesClient: MessagesClient
) : CoroutineWorker(context, workerParameters) {
    private val strategy = Strategy.Builder()
        .setDistanceType(Strategy.DISTANCE_TYPE_EARSHOT)
        .build()

    private val publishOptions = PublishOptions.Builder()
        .setStrategy(strategy)
        .build()

    override suspend fun doWork(): Result = coroutineScope {
        val message = Message(deviceId.toByteArray())

        repeat(NUM_MESSAGES) {
            Log.i("Read", "Sending message #$it")
            val task = messagesClient.publish(message)
            task.await()

            if (!task.isSuccessful) {
                task.exception?.printStackTrace()
                Result.failure()
            }

            delay(DELAY)
        }

        Result.success()
    }

    companion object {
        lateinit var deviceId: String
        private const val NUM_MESSAGES = 3
        private const val DELAY = 50000L
    }
}
