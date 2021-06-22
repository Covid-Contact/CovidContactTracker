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

package cat.covidcontact.tracker.common

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import cat.covidcontact.tracker.common.extensions.observeScreenState
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.feature.main.contracts.RequestBluetoothEnableContract
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    abstract val screenStateHandler: ScreenStateHandler<*>
    abstract val viewModel: BaseViewModel

    private val permissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var bluetoothPermission: ActivityResultLauncher<Array<String>>
    private lateinit var enableBluetooth: ActivityResultLauncher<Unit>
    private var onBluetoothGranted: (Map<String, Boolean>) -> Unit = {}

    @Inject
    lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        screenStateHandler.context = requireActivity()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState.observeScreenState(viewLifecycleOwner, screenStateHandler)
        setUpBluetooth()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onLoadNothing()
    }

    protected fun navigate(
        action: NavDirections,
        navController: NavController = findNavController()
    ) {
        navController.navigate(action)
        viewModel.onLoadNothing()
    }

    protected fun navigateUp(navController: NavController = findNavController()) {
        navController.navigateUp()
        viewModel.onLoadNothing()
    }

    protected fun runWithBluetoothPermission(action: (Map<String, Boolean>) -> Unit) {
        onBluetoothGranted = action
        requestBluetoothPermissions()
    }

    protected fun restartBluetoothAction() {
        requestBluetoothPermissions()
    }

    protected open fun showBluetoothInfo() {}

    @SuppressLint("MissingPermission")
    private fun setUpBluetooth() {
        enableBluetooth = registerForActivityResult(
            RequestBluetoothEnableContract()
        ) { isEnabled ->
            if (isEnabled) {
                requestBluetoothPermissions()
            } else {
                showBluetoothInfo()
            }
        }

        bluetoothPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { grantedResult ->
            val isBluetoothGranted = grantedResult[Manifest.permission.BLUETOOTH] ?: false

            if (isBluetoothGranted) {
                if (bluetoothAdapter.isEnabled) {
                    onBluetoothGranted(grantedResult)
                } else {
                    enableBluetooth.launch(Unit)
                }
            } else {
                showBluetoothInfo()
            }
        }
    }

    private fun requestBluetoothPermissions() {
        bluetoothPermission.launch(permissions)
    }
}
