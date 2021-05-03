package cat.covidcontact.tracker.messages

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cat.covidcontact.tracker.common.extensions.generateDeviceId

object BluetoothReceiver : BroadcastReceiver() {
    private val devicesIdMutable: MutableSet<String> = mutableSetOf()
    val devicesId: Set<String>
        get() = devicesIdMutable

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra(
                    BluetoothDevice.EXTRA_DEVICE
                )

                bluetoothDevice?.let { device ->
                    //Log.i("Test", "onReceive: (name, uuids) = (${device.name}, ${device.uuids})")
                    //Log.i(TAG, "onReceive: (name, alias) = (${device.name})")
                    val id = device.address.generateDeviceId()
                    devicesIdMutable.add(id)
                }
            }
        }
    }

    fun resetDevices() {
        devicesIdMutable.clear()
    }
}
