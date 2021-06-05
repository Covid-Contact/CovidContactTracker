package cat.covidcontact.tracker.feature.settings

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import cat.covidcontact.tracker.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        findPreference<DropDownPreference>(APP_LANGUAGE)?.onPreferenceChangeListener = this
        findPreference<SwitchPreference>(ENABLE_DARK_THEME)?.apply {
            onPreferenceChangeListener = this@SettingsFragment
            isChecked = getUiMode() == Configuration.UI_MODE_NIGHT_YES
        }

        val context = requireContext()
        val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        findPreference<Preference>(VERSION)?.summary = versionName
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        Log.i("Settings", "onPreferenceChange: ${preference?.key}")
        return true
    }

    private fun getUiMode() =
        requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    companion object {
        private const val APP_LANGUAGE = "app_language"
        private const val ENABLE_DARK_THEME = "enable_dark_theme"
        private const val VERSION = "version"
        private const val LOG_OUT = "log_out"
        private const val DELETE_ACCOUNT = "delete_account"
    }
}
