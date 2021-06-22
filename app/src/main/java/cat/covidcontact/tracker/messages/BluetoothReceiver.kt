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
