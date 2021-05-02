package cat.covidcontact.tracker.feature.main

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cat.covidcontact.model.Device
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.generateDeviceId
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentMainBinding
import cat.covidcontact.tracker.feature.main.contracts.RequestBluetoothEnableContract
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("HardwareIds")
@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController
    private val args: MainFragmentArgs by navArgs()

    private val permissions = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN
    )

    private lateinit var bluetoothPermission: ActivityResultLauncher<Array<String>>
    private lateinit var enableBluetooth: ActivityResultLauncher<Unit>
    private var onBluetoothGranted: () -> Unit = {}

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override val viewModel: MainViewModel by activityViewModels()
    override val screenStateHandler = ScreenStateHandler<MainState> { context, state ->
        when (state) {
            is MainState.UserInfoNotFound -> {
                val action = MainFragmentDirections.actionMainFragmentToWelcomeFragment(args.email)
                navigate(action)
            }
            is MainState.UserInfoFound -> {
                val device = Device(
                    bluetoothAdapter.address.generateDeviceId(),
                    bluetoothAdapter.name
                )
                viewModel.onRegisterDevice(state.user, device)
            }
            MainState.DeviceRegistered -> {
                binding.bind()
            }
            MainState.BluetoothInfo -> {
                context.showDialog(
                    title = R.string.bluetooth_info_title,
                    message = R.string.bluetooth_info_message,
                    positiveButtonText = R.string.enable_bluetooth_yes,
                    positiveButtonAction = { _, _ -> requestBluetoothPermissions() },
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

        setUpBluetooth()
        startBluetoothDiscovery()
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
                R.id.profileFragment
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
            val isGranted = grantedResult.values.all { it }

            if (isGranted) {
                if (bluetoothAdapter.isEnabled) {
                    onBluetoothGranted()
                } else {
                    enableBluetooth.launch(Unit)
                }
            } else {
                showBluetoothInfo()
            }
        }
    }

    private fun startBluetoothDiscovery() = runWithBluetoothPermission {

    }

    private fun runWithBluetoothPermission(action: () -> Unit) {
        onBluetoothGranted = action
        requestBluetoothPermissions()
    }

    private fun requestBluetoothPermissions() {
        bluetoothPermission.launch(permissions)
    }

    private fun showBluetoothInfo() {
        viewModel.onLoadBluetoothInfo()
    }
}
