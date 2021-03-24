package cat.covidcontact.tracker.authactivity.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cat.covidcontact.data.services.UserService
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentLogInBinding
import cat.covidcontact.tracker.extensions.getStringWithParams
import cat.covidcontact.tracker.extensions.isEmpty
import cat.covidcontact.tracker.extensions.observeScreenState
import cat.covidcontact.tracker.extensions.showError
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val viewModel: LogInViewModel by viewModels()

    private var isEmailBeingChecked = false
    private var isPasswordBeingChecked = false
    private val screenStateHandler = ScreenStateHandler<LogInState> { state ->
        when (state) {
            LogInState.ChangeToSignUp -> {
                val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            is LogInState.EmailNotFound -> {
            }
            LogInState.WrongPassword -> {
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
            /*val email = binding.logInEmailLayout.editText.toString()
            val password = binding.logInPasswordLayout.editText.toString()

            viewModel.onMakeLogIn(email, password)*/

            lifecycleScope.launch {
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
            }
        }
    }

    private fun LogInViewModel.observe() {
        anyEmptyField.observe(viewLifecycleOwner, ::emptyFieldObserver)
        isEmailInvalid.observe(viewLifecycleOwner, ::invalidEmailObserver)
        isPasswordInvalid.observe(viewLifecycleOwner, ::invalidPasswordObserver)
        screenState.observeScreenState(viewLifecycleOwner, screenStateHandler)
    }

    private fun invalidEmailObserver(isInvalid: Boolean) {
        isEmailBeingChecked = isInvalid
        binding.logInEmailLayout.editText?.addTextChangedListener(
            onTextChanged = { email, _, _, _ ->
                if (isEmailBeingChecked) {
                    viewModel.onVerifyEmail(email.toString())
                }
            }
        )
    }

    private fun invalidPasswordObserver(isInvalid: Boolean) {
        isPasswordBeingChecked = isInvalid
        binding.logInPasswordLayout.editText?.addTextChangedListener(
            onTextChanged = { password, _, _, _ ->
                if (isPasswordBeingChecked) {
                    viewModel.onVerifyPassword(password.toString())
                }
            }
        )
    }

    private fun emptyFieldObserver(isEmpty: Boolean) {
        if (isEmpty) {
            showErrorInLayout(binding.logInEmailLayout, R.string.not_empty, R.string.email)
            showErrorInLayout(binding.logInPasswordLayout, R.string.not_empty, R.string.password)
        } else {
            showErrorInLayout(binding.logInEmailLayout)
            showErrorInLayout(binding.logInPasswordLayout)
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
}