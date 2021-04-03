package cat.covidcontact.tracker.feature.userinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import cat.covidcontact.model.Gender
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

    private fun FragmentBasicUserInfoBinding.bind() {
        val genders = Gender.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, genders)
        (genderLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)

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

        usernameLayout.runWhenTextChanged(::enableNextButton)
        usernameLayout.makeRequired(true)

        genderLayout.runWhenTextChanged(::enableNextButton)
        genderLayout.makeRequired(true)

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
    }

    private fun enableNextButton() {
        binding.btnBasicInformationNext.enableIf(viewModel::onCheckBasicInfo)
    }
}
