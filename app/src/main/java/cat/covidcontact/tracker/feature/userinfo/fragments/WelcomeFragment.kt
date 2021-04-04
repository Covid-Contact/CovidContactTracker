package cat.covidcontact.tracker.feature.userinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.databinding.FragmentWelcomeBinding
import cat.covidcontact.tracker.feature.userinfo.UserInfoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : UserInfoFragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private val args: WelcomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
        viewModel.inputEmail = args.email
    }

    override fun setUpExistingData(user: User) {}

    private fun FragmentWelcomeBinding.bind() {
        btnWelcomeNext.setOnClickListener {
            val action = WelcomeFragmentDirections
                .actionWelcomeFragmentToBasicUserInfoFragment()
            viewModel.onNextFragment(action)
        }
    }
}
