package cat.covidcontact.tracker.feature.contactnetworksettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentContactNetworksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactNetworkSettingsFragment : BaseFragment() {
    private lateinit var binding: FragmentContactNetworksBinding
    private val args: ContactNetworkSettingsFragmentArgs by navArgs()

    override val viewModel: ContactNetworkSettingsViewModel by viewModels()
    override val screenStateHandler =
        ScreenStateHandler<ContactNetworkSettingsState> { context, state ->

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentContactNetworksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.contactNetwork.value = args.contactNetwork
    }
}
