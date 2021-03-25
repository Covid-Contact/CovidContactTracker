package cat.covidcontact.tracker.authactivity.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.databinding.FragmentSignUpBinding
import cat.covidcontact.tracker.util.extensions.observeInvalidField
import cat.covidcontact.tracker.util.extensions.observeScreenState
import cat.covidcontact.tracker.util.handlers.ScreenStateHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    private var isEmailBeingChecked = false
    private var isPasswordBeingChecked = false
    private var arePasswordEqualsBeingChecked = false
    private val screenStateHandler = ScreenStateHandler<SignUpState>() { context, state ->
        when (state) {
            SignUpState.ChangeToLogIn -> {

            }
            is SignUpState.EmailAlreadyRegistered -> {

            }
            is SignUpState.VerifyEmailSent -> {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bind()
        viewModel.observe()
    }

    private fun FragmentSignUpBinding.bind() {
        txtMakeLogIn.setOnClickListener {
            viewModel.onChangeToLogIn()
        }

        btnSignUp.setOnClickListener {
            val email = signUpEmailLayout.editText?.text.toString()
            val password = signUpPasswordLayout.editText?.text.toString()
            val repeatPassword = signUpRepeatPasswordLayout.editText?.text.toString()

            viewModel.onMakeSignUp(email, password, repeatPassword)
        }
    }

    private fun SignUpViewModel.observe() {
        isEmailInvalid.observeInvalidField(
            viewLifecycleOwner,
            binding.signUpEmailLayout,
            getString(R.string.invalid_email),
            viewModel::onVerifyEmail
        )
        isPasswordInvalid.observeInvalidField(
            viewLifecycleOwner,
            binding.signUpPasswordLayout,
            getString(R.string.invalid_password),
            viewModel::onVerifyPassword
        )
        arePasswordsEquals.observeInvalidField(
            viewLifecycleOwner,
            binding.signUpRepeatPasswordLayout,
            getString(R.string.passwords_not_equals),
            viewModel::onVerifyRepeatedPassword
        )
        screenState.observeScreenState(viewLifecycleOwner, screenStateHandler)
    }
}
