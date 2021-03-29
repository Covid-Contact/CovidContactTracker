package cat.covidcontact.tracker.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.hideKeyboard
import cat.covidcontact.tracker.common.extensions.observeInvalidField
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentLogInBinding
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class LogInFragment : BaseFragment() {
    private lateinit var binding: FragmentLogInBinding

    override val viewModel: LogInViewModel by viewModels()
    override val screenStateHandler = ScreenStateHandler<LogInState> { context, state ->
        when (state) {
            LogInState.ChangeToSignUp -> {
                val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            is LogInState.EmailNotFound -> {
            }
            LogInState.WrongPassword -> {
                context.showDialog(R.string.wrong_password_title, R.string.wrong_password_message)
            }
            is LogInState.EmailNotValidated -> {
            }
            is LogInState.SuccessLogIn -> {
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
        viewModel.observe()
    }

    private fun FragmentLogInBinding.bind() {
        txtMakeSignUp.setOnClickListener {
            viewModel.onChangeToSignUp()
        }

        btnLogIn.setOnClickListener {
            hideKeyboard()
            val email = logInEmailLayout.editText?.text.toString()
            val password = logInPasswordLayout.editText?.text.toString()

            viewModel.onMakeLogIn(email, password)
        }
    }


    private fun LogInViewModel.observe() {
        isEmailInvalid.observeInvalidField(
            viewLifecycleOwner,
            binding.logInEmailLayout,
            getString(R.string.invalid_email),
            viewModel::onVerifyEmail
        )
        isPasswordInvalid.observeInvalidField(
            viewLifecycleOwner,
            binding.logInPasswordLayout,
            getString(R.string.invalid_password),
            viewModel::onVerifyPassword
        )
    }

    data class UserPassword(
        @SerializedName("username")
        @Expose
        val username: String,
        @SerializedName("password")
        @Expose
        val password: String
    ) : Serializable

    data class UserEmail(
        var username: String,
        var password: String
    ) : Serializable
}
