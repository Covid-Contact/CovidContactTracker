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

package cat.covidcontact.tracker.feature.contactnetworksettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
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
    private val mainViewModel: MainViewModel by activityViewModels()

    override val viewModel: ContactNetworkSettingsViewModel by viewModels()
    override val screenStateHandler =
        ScreenStateHandler<ContactNetworkSettingsState> { context, state ->
            when (state) {
                is ContactNetworkSettingsState.AccessCodeGenerated -> {
                    binding.txtAccessCode.text = state.accessCode
                }
                is ContactNetworkSettingsState.ContactNetworkDeleted -> {
                    val userDevice = mainViewModel.requireUserDevice()
                    userDevice.user.removeContactNetwork(state.contactNetwork)

                    val navHostFragment = parentFragment as? NavHostFragment
                    navHostFragment?.navController?.let { navController ->
                        navigateUp(navController)
                    }
                }
            }
        }

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

        btnDeleteContactNetwork.setOnClickListener {
            viewModel.onDeleteContactNetwork(mainViewModel.requireUserDevice().user)
        }
    }
}
