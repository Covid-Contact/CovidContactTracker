package cat.covidcontact.tracker.feature.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.hideKeyboard
import cat.covidcontact.tracker.common.extensions.observeInvalidField
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private lateinit var binding: FragmentSignUpBinding
    override val viewModel: SignUpViewModel by viewModels()

    private lateinit var navController: NavController

    override val screenStateHandler = ScreenStateHandler<SignUpState> { context, state ->
        when (state) {
            SignUpState.ChangeToLogIn -> {
                navigateToLogIn()
            }
            is SignUpState.EmailAlreadyRegistered -> {
                context.showDialog(
                    title = context.getString(R.string.email_already_registered_title),
                    message = context.getString(
                        R.string.email_already_registered_message,
                        state.email
                    ),
                    positiveButtonText = context.getString(R.string.log_in),
                    positiveButtonAction = { _, _ -> navigateToLogIn() }
                )
            }
            is SignUpState.VerifyEmailSent -> {
                context.showDialog(
                    title = context.getString(R.string.verify_email_title),
                    message = context.getString(R.string.verify_email_message, state.email),
                    positiveButtonText = context.getString(R.string.log_in),
                    positiveButtonAction = { _, _ -> navigateToLogIn() }
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        navController = findNavController()
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
            makeSignUp()
        }

        signUpRepeatPasswordLayout.editText?.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_GO -> {
                    makeSignUp()
                    true
                }
                else -> false
            }
        }
    }

    private fun FragmentSignUpBinding.makeSignUp() {
        hideKeyboard()
        val email = signUpEmailLayout.editText?.text.toString()
        val password = signUpPasswordLayout.editText?.text.toString()
        val repeatPassword = signUpRepeatPasswordLayout.editText?.text.toString()

        viewModel.onMakeSignUp(email, password, repeatPassword)
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
        arePasswordsDiferent.observeInvalidField(
            viewLifecycleOwner,
            binding.signUpRepeatPasswordLayout,
            getString(R.string.passwords_not_equals),
            viewModel::onVerifyRepeatedPassword
        )
    }

    private fun navigateToLogIn() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
        navigate(action)
    }
}
