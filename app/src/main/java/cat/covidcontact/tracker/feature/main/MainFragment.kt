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
import cat.covidcontact.model.Device
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.generateDeviceId
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentMainBinding
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

    private fun getCurrentAddress(): String {
        val contentResolver = requireContext().contentResolver
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun startBluetoothDiscovery() = runWithBluetoothPermission {
        viewModel.onConfigureMessageClient()

        /*val messageListener = object : MessageListener() {
            override fun onFound(message: Message) {
                Log.i("Test", "onFound: ${String(message.content)}")
            }
        }

        messagesClient.subscribe(messageListener).addOnSuccessListener {
            Log.i("Test", "startBluetoothDiscovery: Subscribe success")
        }.addOnFailureListener {
            Log.i("Test", "startBluetoothDiscovery: Subscribe failure")
            it.printStackTrace()
        }

        binding.btnPublish.setOnClickListener {
            val message = Message("Hello".toByteArray())
            messagesClient.publish(message).addOnSuccessListener {
                Log.i("Test", "startBluetoothDiscovery: Publish success")
            }.addOnFailureListener {
                Log.i("Test", "startBluetoothDiscovery: Publish failure")
                it.printStackTrace()
            }.addOnCompleteListener {
                Log.i("Test", "startBluetoothDiscovery: Publish completed")
            }
        }*/

        /*val bluetoothWork = OneTimeWorkRequestBuilder<DetectUsersWorker>().build()
        workManager.enqueueUniqueWork("bluetoothWork", ExistingWorkPolicy.REPLACE, bluetoothWork)*/

        /*val testRequest = OneTimeWorkRequestBuilder<TestWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork("test", ExistingWorkPolicy.REPLACE, testRequest)*/
    }
}
