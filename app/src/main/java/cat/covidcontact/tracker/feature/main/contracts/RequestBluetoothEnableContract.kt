package cat.covidcontact.tracker.feature.main.contracts

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class RequestBluetoothEnableContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit?): Intent =
        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
        resultCode == Activity.RESULT_OK
}
