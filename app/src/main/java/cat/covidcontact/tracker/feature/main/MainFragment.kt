package cat.covidcontact.tracker.feature.main

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import cat.covidcontact.model.Device
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.navigate
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("HardwareIds")
@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding
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
                val device = Device(bluetoothAdapter.address, bluetoothAdapter.name)
                viewModel.onRegisterDevice(state.user, device)
            }
            MainState.DeviceRegistered -> {
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe()
    }

    private fun MainViewModel.observe() {

    }
}