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

package cat.covidcontact.tracker.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import cat.covidcontact.model.user.Marriage
import cat.covidcontact.model.user.Occupation
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.addOnTextChanged
import cat.covidcontact.tracker.common.extensions.observeText
import cat.covidcontact.tracker.common.extensions.setExposedMenuItems
import cat.covidcontact.tracker.common.extensions.setText
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentProfileBinding
import cat.covidcontact.tracker.feature.main.MainViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override val viewModel: ProfileViewModel by viewModels()
    override val screenStateHandler = ScreenStateHandler<ProfileState> { context, state ->
        when (state) {
            ProfileState.ProfileEdited -> {
                binding.bind(mainViewModel.requireUserDevice().user)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.setUpObservers()
    }

    private fun MainViewModel.setUpObservers() {
        userDevice.observe(viewLifecycleOwner) { userDevice ->
            binding.bind(userDevice.user)
        }
    }

    private fun FragmentProfileBinding.bind(user: User) {
        cityLayout.setUp(user.city, viewModel.city)
        studiesLayout.setUp(user.studies, viewModel.studies)
        occupationLayout.setUp(user.occupation.toString(), viewModel.occupation) {
            setExposedMenuItems<Occupation>(requireContext())
        }
        marriageLayout.setUp(user.marriage.toString(), viewModel.marriage) {
            setExposedMenuItems<Marriage>(requireContext())
        }
        childrenLayout.setUp(user.children.toString(), viewModel.children)

        val options = listOf(
            getString(R.string.yes),
            getString(R.string.no),
            getString(R.string.decline_to_answer)
        )

        positiveLayout.setUpExposedDropdown(options, user.hasBeenPositive, viewModel.positive)
        vaccinatedLayout.setUpExposedDropdown(options, user.isVaccinated, viewModel.vaccinated)

        btnUpdateChanges.setOnClickListener {
            viewModel.onUpdateProfile(mainViewModel.requireUserDevice().user)
        }
    }

    private fun TextInputLayout.setUp(
        text: String?,
        liveData: MutableLiveData<String>,
        onBeforeAdding: TextInputLayout.() -> Unit = {}
    ) {
        onBeforeAdding(this)
        observeText(liveData)
        setText(text)
    }

    private fun TextInputLayout.setUpExposedDropdown(
        options: List<String>,
        value: Boolean?,
        liveData: MutableLiveData<Boolean?>
    ) {
        val textId = when (value) {
            null -> R.string.decline_to_answer
            true -> R.string.yes
            else -> R.string.no
        }

        val text = getString(textId)
        addOnTextChanged { newText ->
            updateBooleanLiveData(liveData, newText.toString())
        }

        setExposedMenuItems(requireContext(), options, selectedValue = text)
        updateBooleanLiveData(liveData, text)
    }

    private fun updateBooleanLiveData(
        liveData: MutableLiveData<Boolean?>,
        newText: String?
    ) {
        liveData.value = when (newText.toString()) {
            getString(R.string.decline_to_answer) -> null
            getString(R.string.yes) -> true
            else -> false
        }
    }
}
