package cat.covidcontact.tracker.feature.userinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.covidcontact.model.user.Marriage
import cat.covidcontact.model.user.Occupation
import cat.covidcontact.tracker.common.extensions.addOnTextChanged
import cat.covidcontact.tracker.common.extensions.setExposedMenuItems
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
}
