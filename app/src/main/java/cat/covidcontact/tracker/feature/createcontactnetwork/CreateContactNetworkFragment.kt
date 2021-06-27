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

package cat.covidcontact.tracker.feature.createcontactnetwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.makeRequired
import cat.covidcontact.tracker.common.extensions.observeEnabled
import cat.covidcontact.tracker.common.extensions.observeText
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentCreateContactNetworkBinding
import cat.covidcontact.tracker.feature.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateContactNetworkFragment : BaseFragment() {
    private lateinit var binding: FragmentCreateContactNetworkBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override val viewModel: CreateContactNetworkViewModel by viewModels()
    override val screenStateHandler =
        ScreenStateHandler<CreateContactNetworkState> { context, state ->
            when (state) {
                is CreateContactNetworkState.ContactNetworkAlreadyExisting -> {
                    context.showDialog(
                        title = R.string.contact_network_existing_title,
                        message = R.string.contact_network_existing_message
                    )
                }
                is CreateContactNetworkState.ContactNetworkCreated -> {
                    navigateUp()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCreateContactNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
    }

    private fun FragmentCreateContactNetworkBinding.bind() {
        txtContactNetworkName.makeRequired(true)
        txtContactNetworkName.observeText(viewModel.name)
        txtContactNetworkPassword.observeText(viewModel.password)

        btnCreateContactNetwork.observeEnabled(viewLifecycleOwner, viewModel.isNameValid)
        btnCreateContactNetwork.setOnClickListener {
            mainViewModel.userDevice.value?.let {
                viewModel.onCreateContactNetwork(it.user)
            }
        }

        btnCreateContactNetwork.isEnabled = false
    }
}
