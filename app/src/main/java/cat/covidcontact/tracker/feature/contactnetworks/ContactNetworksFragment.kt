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

package cat.covidcontact.tracker.feature.contactnetworks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.observeList
import cat.covidcontact.tracker.common.extensions.observeVisible
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentContactNetworksBinding
import cat.covidcontact.tracker.feature.contactnetworks.recyclerview.ContactNetworkAdapter
import cat.covidcontact.tracker.feature.contactnetworks.recyclerview.NetworkCardStateColor
import cat.covidcontact.tracker.feature.contactnetworks.recyclerview.NetworkCardStateText
import cat.covidcontact.tracker.feature.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactNetworksFragment : BaseFragment() {
    private lateinit var binding: FragmentContactNetworksBinding
    private lateinit var adapter: ContactNetworkAdapter

    private val mainViewModel: MainViewModel by activityViewModels()
    override val viewModel: ContactNetworksViewModel by viewModels()
    override val screenStateHandler = ScreenStateHandler<ContactNetworksState> { context, state ->
        when (state) {
            ContactNetworksState.CreateContactNetwork -> {
                val action = ContactNetworksFragmentDirections
                    .actionContactNetworksFragmentToCreateContactNetworkFragment()
                navigate(action)
            }
            is ContactNetworksState.ShowContactNetworkSettings -> {
                val action = ContactNetworksFragmentDirections
                    .actionContactNetworksFragmentToContactNetworkSettingsFragment(
                        state.contactNetwork,
                        state.contactNetwork.name
                    )
                navigate(action)
            }
            ContactNetworksState.ExitContactNetwork -> {
                requireActivity().recreate()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentContactNetworksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        refreshContactNetworkList()
    }

    override fun onResume() {
        super.onResume()
        showFloatingActionButton()
    }

    override fun onPause() {
        super.onPause()
        hideFloatingActionButton()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
        mainViewModel.observe()
    }

    private fun FragmentContactNetworksBinding.bind() {
        contactNetworkList.observeList(viewLifecycleOwner, viewModel.contactNetworks)

        flBtnCreateContactNetwork.hide()
        flBtnCreateContactNetwork.setOnClickListener {
            viewModel.onCreateContactNetworkDialog()
        }

        btnPositive.setOnClickListener {
            requireContext().showDialog(
                title = R.string.positive_dialog_title,
                message = R.string.positive_dialog_message,
                positiveButtonText = R.string.yes,
                positiveButtonAction = { _, _ -> viewModel.onNotifyPositive() },
                negativeButtonText = R.string.no
            )
        }

        layoutNoContactNetworks.observeVisible(
            viewLifecycleOwner,
            viewModel.isNoContactNetworksVisible
        )

        flBtnCreateContactNetwork.show()
    }

    private fun MainViewModel.observe() {
        userDevice.observe(viewLifecycleOwner) { userDevice ->
            binding.flBtnCreateContactNetwork.show()

            adapter = ContactNetworkAdapter(
                requireContext(),
                userDevice.user.username,
                NetworkCardStateColor(),
                NetworkCardStateText(),
                onSettingsClick = { contactNetwork ->
                    viewModel.onShowContactNetworkSettings(contactNetwork)
                },
                onExitContactNetwork = { contactNetwork ->
                    requireContext().showDialog(
                        title = getString(
                            R.string.exit_contact_network_dialog_title,
                            contactNetwork.name
                        ),
                        message = getString(
                            R.string.exit_contact_network_dialog_message,
                            contactNetwork.name
                        ),
                        positiveButtonText = getString(R.string.yes),
                        positiveButtonAction = { _, _ ->
                            viewModel.onExitContactNetwork(contactNetwork)
                        },
                        negativeButtonText = getString(R.string.no)
                    )
                }
            )
            binding.contactNetworkList.adapter = adapter
            binding.contactNetworkList.layoutManager = LinearLayoutManager(requireContext())
            viewModel.onLoadContactNetworks(userDevice.user)
        }
    }

    private fun refreshContactNetworkList() {
        mainViewModel.userDevice.value?.let {
            viewModel.onLoadContactNetworks(it.user)
        }
    }

    private fun showFloatingActionButton() {
        binding.flBtnCreateContactNetwork.show()
    }

    private fun hideFloatingActionButton() {
        binding.flBtnCreateContactNetwork.hide()
    }
}
