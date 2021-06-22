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
import cat.covidcontact.model.user.Gender
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.extensions.*
import cat.covidcontact.tracker.databinding.FragmentBasicUserInfoBinding
import cat.covidcontact.tracker.feature.userinfo.UserInfoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicUserInfoFragment : UserInfoFragment() {
    private lateinit var binding: FragmentBasicUserInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentBasicUserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
    }

    override fun setUpExistingData(user: User) {}

    private fun FragmentBasicUserInfoBinding.bind() {
        genderLayout.setExposedMenuItems<Gender>(requireContext())

        birthDateLayout.setEndIconOnClickListener {
            showCalendarPicker(
                R.string.select_birth_date,
                R.string.birth_date,
                onPositiveButtonClicked = { selectedDate ->
                    viewModel.inputBirthDate = selectedDate
                    birthDateLayout.showDate(selectedDate)
                }
            )
        }

        viewModel.inputUsername?.let { usernameLayout.setText(it) }
        usernameLayout.addOnTextChanged { text ->
            text?.let { viewModel.inputUsername = if (it.isNotEmpty()) it.toString() else null }
            enableNextButton()
        }
        usernameLayout.makeRequired(true)

        viewModel.inputGender?.let { genderLayout.setText(it.name) }
        genderLayout.addOnTextChanged { text ->
            text?.let {
                viewModel.inputGender =
                    if (it.isNotEmpty()) Gender.valueOf(it.toString()) else null
            }
            enableNextButton()
        }
        genderLayout.makeRequired(true)

        viewModel.inputBirthDate?.let { birthDateLayout.showDate(it) }
        birthDateLayout.runWhenTextChanged(::enableNextButton)
        birthDateLayout.makeRequired(true)

        btnBasicInformationNext.setOnClickListener {
            val action = BasicUserInfoFragmentDirections
                .actionBasicUserInfoFragmentToPersonalInformationFragment()
            viewModel.onBasicUserInfoIntroduced(action)
        }

        btnBasicInformationPrevious.setOnClickListener {
            viewModel.onPreviousFragment()
        }

        enableNextButton()
    }

    private fun enableNextButton() {
        binding.btnBasicInformationNext.enableIf(viewModel::onCheckBasicInfo)
    }
}
