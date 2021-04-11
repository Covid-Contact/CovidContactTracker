package cat.covidcontact.tracker.feature.contactnetworks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.makeRequired
import cat.covidcontact.tracker.common.extensions.observeInvalidField
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.DialogCreateContactNetworkBinding
import cat.covidcontact.tracker.databinding.FragmentContactNetworksBinding
import cat.covidcontact.tracker.feature.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactNetworksFragment : BaseFragment() {
    private lateinit var binding: FragmentContactNetworksBinding
    private lateinit var dialogBinding: DialogCreateContactNetworkBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    override val viewModel: ContactNetworksViewModel by viewModels()
    override val screenStateHandler = ScreenStateHandler<ContactNetworksState> { context, state ->
        when (state) {
            ContactNetworksState.CreateContactNetwork -> {
                CreateContactNetworkDialog.showDialog(requireContext(), dialogBinding)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentContactNetworksBinding.inflate(inflater, container, false)
        dialogBinding = DialogCreateContactNetworkBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
        dialogBinding.bind()
        viewModel.observe()
    }

    private fun FragmentContactNetworksBinding.bind() {
        btnCreateContactNetwork.setOnClickListener {
            viewModel.onCreateContactNetworkDialog()
        }
    }

    private fun DialogCreateContactNetworkBinding.bind() {
        txtContactNetworkName.makeRequired(true)
        txtConfirmContactNetworkCreation.setOnClickListener {
            val name = txtContactNetworkName.editText?.text.toString()
            val password = txtContactNetworkPassword.editText?.text.toString()
            val isNameValid = viewModel.onValidateName(name)

            if (isNameValid) {
                CreateContactNetworkDialog.dismissDialog()
                viewModel.onCreateContactNetwork(
                    name,
                    password,
                    mainViewModel.userDevice.user.username
                )
            }
        }
    }

    private fun ContactNetworksViewModel.observe() {
        isNameEmpty.observeInvalidField(
            viewLifecycleOwner,
            dialogBinding.txtContactNetworkName,
            getString(R.string.contact_network_name_cannot_be_empty),
            viewModel::onValidateName
        )
    }
}
