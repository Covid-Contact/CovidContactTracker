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

package cat.covidcontact.tracker.feature.userinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.extensions.addOnTextChanged
import cat.covidcontact.tracker.common.extensions.setExposedMenuItems
import cat.covidcontact.tracker.common.extensions.setText
import cat.covidcontact.tracker.databinding.FragmentCovidInformationBinding
import cat.covidcontact.tracker.feature.userinfo.UserInfoFragment

class CovidInformationFragment : UserInfoFragment() {
    private lateinit var binding: FragmentCovidInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentCovidInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
    }

    override fun setUpExistingData(user: User) {
        binding.bindUserInfo(user)
    }

    private fun FragmentCovidInformationBinding.bind() {
        val options = listOf(
            getString(R.string.yes),
            getString(R.string.no),
            getString(R.string.decline_to_answer)
        )

        positiveLayout.setExposedMenuItems(requireContext(), options)
        vaccinatedLayout.setExposedMenuItems(requireContext(), options)

        viewModel.currentUser?.let { user ->
            positiveLayout.addOnTextChanged { text ->
                text?.let {
                    user.hasBeenPositive = it.toString() != getString(R.string.decline_to_answer)
                }
            }

            vaccinatedLayout.addOnTextChanged { text ->
                text?.let {
                    user.isVaccinated = it.toString() != getString(R.string.decline_to_answer)
                }
            }
        }

        btnConfirmCreation.setOnClickListener {
            viewModel.onUserCreated()
        }

        btnCovid19InformationPrevious.setOnClickListener {
            viewModel.onPreviousFragment()
        }
    }

    private fun FragmentCovidInformationBinding.bindUserInfo(user: User) {
        user.hasBeenPositive?.let { hasBeenPositive ->
            val textId = if (hasBeenPositive) R.string.yes else R.string.no
            positiveLayout.setText(textId)
        }

        user.isVaccinated?.let { isVaccinated ->
            val textId = if (isVaccinated) R.string.yes else R.string.no
            vaccinatedLayout.setText(textId)
        }
    }
}
