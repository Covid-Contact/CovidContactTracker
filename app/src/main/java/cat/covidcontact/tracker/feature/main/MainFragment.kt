package cat.covidcontact.tracker.feature.main

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
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
                val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                val device = Device(
                    bluetoothAdapter.address.generateDeviceId(),
                    bluetoothAdapter.name
                )
                viewModel.onRegisterDevice(state.user, device)
            }
            MainState.DeviceRegistered -> {
                binding.bind()
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
}
