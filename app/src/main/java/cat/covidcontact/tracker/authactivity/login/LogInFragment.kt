package cat.covidcontact.tracker.authactivity.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.databinding.FragmentLogInBinding
import cat.covidcontact.tracker.extensions.getStringWithParams
import cat.covidcontact.tracker.extensions.isEmpty
import cat.covidcontact.tracker.extensions.showError
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val viewModel: LogInViewModel by viewModels()

    private var isEmailBeingChecked = false
    private var isPasswordBeingChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.anyEmptyField.observe(viewLifecycleOwner, ::emptyFieldObserver)
        viewModel.isEmailInvalid.observe(viewLifecycleOwner, ::invalidEmailObserver)
        viewModel.isPasswordInvalid.observe(viewLifecycleOwner, ::invalidPasswordObserver)
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