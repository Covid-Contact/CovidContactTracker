package cat.covidcontact.tracker.authactivity.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.databinding.FragmentLogInBinding
import cat.covidcontact.tracker.util.extensions.observeInvalidField
import cat.covidcontact.tracker.util.extensions.observeScreenState
import cat.covidcontact.tracker.util.extensions.showDialog
import cat.covidcontact.tracker.util.handlers.ScreenStateHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val viewModel: LogInViewModel by viewModels()

    private var isEmailBeingChecked = false
    private var isPasswordBeingChecked = false
    private val screenStateHandler = ScreenStateHandler<LogInState> { context, state ->
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
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        screenStateHandler.context = requireContext()
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
            val email = logInEmailLayout.editText?.text.toString()
            val password = logInPasswordLayout.editText?.text.toString()

            viewModel.onMakeLogIn(email, password)

            /*lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val url = "http://covidcontact.cat:8080/user/signup"
                    val ep =
                        UserService.EmailPassword("apinto.developer@gmail.com", "Barcelona2020$")
                    val body = Gson().toJson(ep)

                    val (request, response, result) = url.httpPost()
                        .jsonBody(body)
                        .responseString()
                    Log.i("Test", "onViewCreated: $request")
                    Log.i("Test", "onViewCreated: $result")
                }
            }*/
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
        screenState.observeScreenState(viewLifecycleOwner, screenStateHandler)
    }
}
