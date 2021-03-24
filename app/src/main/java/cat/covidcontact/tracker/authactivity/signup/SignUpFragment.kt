package cat.covidcontact.tracker.authactivity.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.databinding.FragmentSignUpBinding
import cat.covidcontact.tracker.extensions.getStringWithParams
import cat.covidcontact.tracker.extensions.isEmpty
import cat.covidcontact.tracker.extensions.showError
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    private var isEmailBeingChecked = false
    private var isPasswordBeingChecked = false
    private var arePasswordEqualsBeingChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.anyEmptyField.observe(viewLifecycleOwner, ::emptyFieldObserver)
        viewModel.isEmailInvalid.observe(viewLifecycleOwner, ::invalidEmailObserver)
        viewModel.isPasswordInvalid.observe(viewLifecycleOwner, ::invalidPasswordObserver)
        viewModel.arePasswordsEquals.observe(viewLifecycleOwner, ::equalsPasswordObserver)
        viewModel.screenState.observe(viewLifecycleOwner, ::stateObserver)

        binding.txtMakeLogIn.setOnClickListener {
            viewModel.onChangeToLogIn()
        }

        binding.btnSignUp.setOnClickListener {
            val email = binding.signUpEmailLayout.editText?.text.toString()
            val password = binding.signUpPasswordLayout.editText?.text.toString()
            val repeatPassword = binding.signUpRepeatPasswordLayout.editText?.text.toString()

            viewModel.onMakeSignUp(email, password, repeatPassword)
        }
    }

    private fun invalidEmailObserver(isInvalid: Boolean) {
        isEmailBeingChecked = isInvalid
        binding.signUpEmailLayout.editText?.addTextChangedListener(
            onTextChanged = { email, _, _, _ ->
                if (isEmailBeingChecked) {
                    viewModel.onVerifyEmail(email.toString())
                }
            }
        )
    }

    private fun invalidPasswordObserver(isInvalid: Boolean) {
        isPasswordBeingChecked = isInvalid
        binding.signUpPasswordLayout.editText?.addTextChangedListener(
            onTextChanged = { password, _, _, _ ->
                if (isPasswordBeingChecked) {
                    viewModel.onVerifyPassword(password.toString())
                }
            }
        )
    }

    private fun equalsPasswordObserver(arePasswordEquals: Boolean) {
        arePasswordEqualsBeingChecked = !arePasswordEquals
        binding.signUpRepeatPasswordLayout.editText?.addTextChangedListener(
            onTextChanged = { repeatedPassword, _, _, _ ->
                if (arePasswordEqualsBeingChecked) {
                    viewModel.onVerifyRepeatedPasswords(
                        binding.signUpPasswordLayout.editText?.text.toString(),
                        repeatedPassword.toString()
                    )
                }
            }
        )
    }

    private fun emptyFieldObserver(isEmpty: Boolean) {
        if (isEmpty) {
            showErrorInLayout(binding.signUpEmailLayout, R.string.not_empty, R.string.email)
            showErrorInLayout(binding.signUpPasswordLayout, R.string.not_empty, R.string.password)
        } else {
            showErrorInLayout(binding.signUpEmailLayout)
            showErrorInLayout(binding.signUpPasswordLayout)
        }
    }

    private fun showErrorInLayout(
        textInputLayout: TextInputLayout,
        id: Int? = null,
        vararg args: Int
    ) {
        val msg = if (textInputLayout.isEmpty()) {
            requireContext().getStringWithParams(id!!, args)
        } else {
            null
        }

        textInputLayout.showError(msg)
    }

    private fun stateObserver(screenState: ScreenState) {
        when (screenState) {
            ScreenState.Loading -> {
            }
            ScreenState.NoInternet -> {
            }
            ScreenState.OtherError -> {
            }
            else -> signUpObserver(screenState as SignUpState)
        }
    }

    private fun signUpObserver(signUpState: SignUpState) {
        when (signUpState) {
            SignUpState.ChangeToLogIn -> {
                val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
                findNavController().navigate(action)
            }
            is SignUpState.EmailAlreadyRegistered -> {
            }
            is SignUpState.VerifyCodeSent -> {
            }
        }
    }
}