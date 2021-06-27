/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cat.covidcontact.tracker.messages

import android.annotation.SuppressLint
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

            if (onCheckBluetoothPermission(context)) {
                val task = messagesClient.publish(message)
                task.await()

                if (!task.isSuccessful) {
                    task.exception?.printStackTrace()
                    Result.failure()
                }
            }

            delay(DELAY)
        }

        Result.success()
    }

    @SuppressLint("StaticFieldLeak")
    companion object {
        lateinit var deviceId: String
        lateinit var context: Context
        var onCheckBluetoothPermission: (Context) -> Boolean = { true }
        private const val NUM_MESSAGES = 3
        private const val DELAY = 50000L
    }
}
