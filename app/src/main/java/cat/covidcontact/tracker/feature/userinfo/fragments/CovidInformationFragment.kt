package cat.covidcontact.tracker.feature.userinfo.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.extensions.addOnTextChanged
import cat.covidcontact.tracker.common.extensions.setExposedMenuItems
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

    private fun FragmentCovidInformationBinding.bind() {
        val options = listOf(
            getString(R.string.yes),
            getString(R.string.no),
            getString(R.string.decline_to_answer)
        )
        positiveLayout.setExposedMenuItems(requireContext(), options)
        vaccinatedLayout.setExposedMenuItems(requireContext(), options)

        /*positiveLayout.editText?.setText(positiveAdapter.getItem(2))
        vaccinatedLayout.editText?.setText(vaccinatedAdapter.getItem(2))*/

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
            Log.i("Test", "bind: ${viewModel.currentUser}")
        }

        btnCovid19InformationPrevious.setOnClickListener {
            viewModel.onPreviousFragment()
        }
    }
}
