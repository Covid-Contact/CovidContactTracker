package cat.covidcontact.tracker.messages

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class SendCodeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {

        Result.success()
    }

    companion object {
        lateinit var onSend: () -> Unit
    }
}
