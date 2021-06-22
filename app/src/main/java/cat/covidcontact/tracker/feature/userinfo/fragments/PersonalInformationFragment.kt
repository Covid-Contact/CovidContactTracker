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
import cat.covidcontact.model.user.Marriage
import cat.covidcontact.model.user.Occupation
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.common.extensions.addOnTextChanged
import cat.covidcontact.tracker.common.extensions.setExposedMenuItems
import cat.covidcontact.tracker.common.extensions.setText
import cat.covidcontact.tracker.databinding.FragmentPersonalInformationBinding
import cat.covidcontact.tracker.feature.userinfo.UserInfoFragment

class PersonalInformationFragment : UserInfoFragment() {
    private lateinit var binding: FragmentPersonalInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPersonalInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
    }

    override fun setUpExistingData(user: User) {
        binding.bindUserInfo(user)
    }

    private fun FragmentPersonalInformationBinding.bind() {
        viewModel.currentUser?.let { user ->
            cityLayout.addOnTextChanged { text ->
                text?.let { user.city = if (it.isNotEmpty()) it.toString() else null }
            }
            studiesLayout.addOnTextChanged { text ->
                text?.let { user.studies = if (it.isNotEmpty()) it.toString() else null }
            }

            occupationLayout.setExposedMenuItems<Occupation>(requireContext())
            occupationLayout.addOnTextChanged { text ->
                text?.let {
                    user.occupation =
                        if (it.isNotEmpty()) Occupation.valueOf(it.toString()) else null
                }
            }

            marriageLayout.setExposedMenuItems<Marriage>(requireContext())
            marriageLayout.addOnTextChanged { text ->
                text?.let {
                    user.marriage = if (it.isNotEmpty()) Marriage.valueOf(it.toString()) else null
                }
            }
            childrenLayout.addOnTextChanged { text ->
                text?.let { user.children = if (it.isNotEmpty()) it.toString().toInt() else null }
            }
        }

        btnPersonalInformationNext.setOnClickListener {
            val action = PersonalInformationFragmentDirections
                .actionPersonalInformationFragmentToCovidInformationFragment()
            viewModel.onNextFragment(action)
        }

        btnPersonalInformationPrevious.setOnClickListener {
            viewModel.onPreviousFragment()
        }
    }

    private fun FragmentPersonalInformationBinding.bindUserInfo(user: User) {
        user.city?.let { cityLayout.setText(it) }
        user.studies?.let { studiesLayout.setText(it) }
        user.occupation?.let { occupationLayout.setText(it.name) }
        user.marriage?.let { marriageLayout.setText(it.name) }
        user.children?.let { childrenLayout.setText(it.toString()) }
    }
}
