package cat.covidcontact.tracker.messages

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@HiltWorker
class FinishInteractionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        delay(DELAY)
        onSend()
        Result.success()
    }

    companion object {
        lateinit var onSend: () -> Unit
        private const val DELAY = 50000L
    }
}
