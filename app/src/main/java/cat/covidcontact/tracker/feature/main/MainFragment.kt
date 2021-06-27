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

package cat.covidcontact.tracker.feature.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import cat.covidcontact.model.Device
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.generateDeviceId
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentMainBinding
import cat.covidcontact.tracker.feature.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("HardwareIds")
@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController
    private val args: MainFragmentArgs by navArgs()

    override val viewModel: MainViewModel by activityViewModels()
    override val screenStateHandler = ScreenStateHandler<MainState> { context, state ->
        when (state) {
            is MainState.UserInfoNotFound -> {
                val action = MainFragmentDirections.actionMainFragmentToWelcomeFragment(args.email)
                navigate(action)
            }
            is MainState.UserInfoFound -> {
                val address = getCurrentAddress()
                val device = Device(
                    address.generateDeviceId(),
                    bluetoothAdapter.name
                )
                viewModel.onRegisterDevice(state.user, device)
            }
            MainState.DeviceRegistered -> {
                viewModel.onSendMessagingToken()
            }
            MainState.MessagingTokenSent -> {
                binding.bind()
                startBluetoothDiscovery()
            }
            MainState.BluetoothInfo -> {
                context.showDialog(
                    title = R.string.bluetooth_info_title,
                    message = R.string.bluetooth_info_message,
                    positiveButtonText = R.string.enable_bluetooth_yes,
                    positiveButtonAction = { _, _ -> restartBluetoothAction() },
                    negativeButtonText = R.string.enable_bluetooth_no,
                    isCancelable = false
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.onGetCurrentUser(args.email)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        viewModel.onSetSkip(sharedPreferences.getBoolean(SettingsFragment.SKIP, false))
    }

    override fun showBluetoothInfo() {
        super.showBluetoothInfo()
        viewModel.onLoadBluetoothInfo()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onFinishInteraction()
    }

    private fun FragmentMainBinding.bind() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.mainFragmentNavGraph)
            as NavHostFragment
        navController = navHostFragment.findNavController()
        bottomNavigationView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.contactNetworksFragment,
                R.id.searchFragment,
                R.id.profileFragment,
                R.id.settingsFragment
            )
        )

        (requireActivity() as AppCompatActivity).let {
            it.setSupportActionBar(topBar)
            it.setupActionBarWithNavController(navController, appBarConfiguration)
        }

        topBar.setNavigationOnClickListener {
            navigateUp(navController)
        }
    }

    private fun getCurrentAddress(): String {
        val contentResolver = requireContext().contentResolver
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun startBluetoothDiscovery() = runWithBluetoothPermission { grantResult ->
        viewModel.onConfigureMessageClient(requireContext())
    }

    fun navigateToLogin() {
        val action = MainFragmentDirections.actionMainFragmentToLogInFragment()
        navigate(action)
    }
}
