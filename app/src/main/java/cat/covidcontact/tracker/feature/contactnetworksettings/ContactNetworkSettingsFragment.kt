package cat.covidcontact.tracker.feature.contactnetworksettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.observeChecked
import cat.covidcontact.tracker.common.extensions.observeEnabled
import cat.covidcontact.tracker.common.extensions.observeVisible
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentContactNetworkSettingsBinding
import cat.covidcontact.tracker.feature.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactNetworkSettingsFragment : BaseFragment() {
    private lateinit var binding: FragmentContactNetworkSettingsBinding
    private val args: ContactNetworkSettingsFragmentArgs by navArgs()

    override val viewModel: ContactNetworkSettingsViewModel by viewModels()
    override val screenStateHandler =
        ScreenStateHandler<ContactNetworkSettingsState> { context, state ->
            when (state) {
                is ContactNetworkSettingsState.AccessCodeGenerated -> {
                    binding.txtAccessCode.text = state.accessCode
                }
            }
        }

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentContactNetworkSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.contactNetwork.value = args.contactNetwork

        binding.bind()
    }

    private fun FragmentContactNetworkSettingsBinding.bind() {
        swEnableUserAddition.observeChecked(viewLifecycleOwner, viewModel.isVisibleChecked)
        swEnableUserAddition.setOnCheckedChangeListener { view, isChecked ->
            viewModel.onEnableUsersAddition(isChecked)
        }

        swEnablePasswordAddition.observeEnabled(viewLifecycleOwner, viewModel.isPasswordEnabled)
        swEnablePasswordAddition.observeChecked(viewLifecycleOwner, viewModel.isPasswordChecked)

        txtAccessCode.observeVisible(viewLifecycleOwner, viewModel.isAccessCodeGenerated)
        txtAccessCode.text = viewModel.contactNetwork.value?.accessCode ?: ""

        btnGenerateAccessCode.setOnClickListener {
            viewModel.onGenerateAccessCode(mainViewModel.requireUserDevice().user.email)
        }
    }
}
