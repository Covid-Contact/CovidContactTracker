/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cat.covidcontact.tracker.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.hideKeyboard
import cat.covidcontact.tracker.common.extensions.observeInvalidField
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentLogInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogInFragment : BaseFragment() {
    private lateinit var binding: FragmentLogInBinding

    override val viewModel: LogInViewModel by viewModels()
    override val screenStateHandler = ScreenStateHandler<LogInState> { context, state ->
        when (state) {
            LogInState.ChangeToSignUp -> {
                navigateToSignUp()
            }
            is LogInState.EmailNotFound -> {
                context.showDialog(
                    title = context.getString(R.string.email_not_found_title),
                    message = context.getString(R.string.email_not_found_message, state.email),
                    positiveButtonText = context.getString(R.string.sign_up),
                    positiveButtonAction = { _, _ -> navigateToSignUp() }
                )
            }
            LogInState.WrongPassword -> {
                context.showDialog(
                    title = R.string.wrong_password_title,
                    message = R.string.wrong_password_message
                )
            }
            is LogInState.EmailNotValidated -> {
                context.showDialog(
                    title = context.getString(R.string.email_not_validated_title),
                    message = context.getString(R.string.email_not_validated_message, state.email)
                )
            }
            is LogInState.SuccessLogIn -> {
                navigateToMain(state.email)
            }
        }
    }

    private fun navigateToMain(email: String) {
        val action = LogInFragmentDirections.actionLogInFragmentToMainFragment(email)
        navigate(action)
    }

    private fun navigateToSignUp() {
        val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
        navigate(action)
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
            makeLogIn()
        }

        logInPasswordLayout.editText?.setOnEditorActionListener { _, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_GO -> {
                    makeLogIn()
                    true
                }
                else -> false
            }
        }
    }

    private fun FragmentLogInBinding.makeLogIn() {
        val email = logInEmailLayout.editText?.text.toString()
        val password = logInPasswordLayout.editText?.text.toString()

        viewModel.onMakeLogIn(email, password)
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
}
