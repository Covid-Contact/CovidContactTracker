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

package cat.covidcontact.tracker.feature.settings

import android.content.res.Configuration
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseSettingsFragment
import cat.covidcontact.tracker.common.extensions.enableDarkTheme
import cat.covidcontact.tracker.common.extensions.showDialog
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.feature.main.MainFragment
import cat.covidcontact.tracker.feature.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseSettingsFragment(), Preference.OnPreferenceChangeListener,
    Preference.OnPreferenceClickListener {
    override val preferenceResource = R.xml.settings
    override val viewModel: SettingsViewModel by viewModels()
    override val screenStateHandler = ScreenStateHandler<SettingsState> { context, state ->
        when (state) {
            SettingsState.NavigateToLogin -> {
                val navHostFragment = parentFragment as? NavHostFragment
                val mainFragment = navHostFragment?.parentFragment as? MainFragment
                mainFragment?.navigateToLogin()
            }
        }
    }

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onPreferencesCreated() {
        super.onPreferencesCreated()

        findPreference<DropDownPreference>(APP_LANGUAGE)?.onPreferenceChangeListener = this
        findPreference<SwitchPreference>(ENABLE_DARK_THEME)?.apply {
            onPreferenceChangeListener = this@SettingsFragment
            isChecked = getUiMode() == Configuration.UI_MODE_NIGHT_YES
        }

        val context = requireContext()
        val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        findPreference<Preference>(VERSION)?.summary = versionName

        findPreference<Preference>(LOG_OUT)?.onPreferenceClickListener = this
        findPreference<Preference>(DELETE_ACCOUNT)?.onPreferenceClickListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        when (preference?.key) {
            APP_LANGUAGE -> {
                addSkipRead()
                requireActivity().recreate()
            }
            ENABLE_DARK_THEME -> {
                addSkipRead()
                enableDarkTheme(newValue as Boolean)
            }
        }
        return true
    }

    private fun addSkipRead() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val editor = sharedPreferences.edit()
        editor.putBoolean(SKIP, true)
        editor.apply()
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        return when (preference?.key) {
            LOG_OUT -> {
                viewModel.onMakeLogOut(mainViewModel.requireUserDevice())
                true
            }
            DELETE_ACCOUNT -> {
                requireContext().showDialog(
                    title = R.string.delete_account_dialog_title,
                    message = R.string.delete_account_dialog_message,
                    positiveButtonText = R.string.yes,
                    positiveButtonAction = { _, _ ->
                        viewModel.onDeleteAccount(mainViewModel.requireUserDevice().user.email)
                    },
                    negativeButtonText = R.string.no
                )

                true
            }
            else -> false
        }
    }

    private fun getUiMode() =
        requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    companion object {
        const val APP_LANGUAGE = "app_language"
        const val ENABLE_DARK_THEME = "enable_dark_theme"
        const val VERSION = "version"
        const val LOG_OUT = "log_out"
        const val DELETE_ACCOUNT = "delete_account"
        const val SKIP = "skip"
    }
}
