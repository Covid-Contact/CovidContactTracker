package cat.covidcontact.tracker

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import cat.covidcontact.tracker.common.extensions.enableDarkTheme
import cat.covidcontact.tracker.databinding.ActivityMainBinding
import cat.covidcontact.tracker.feature.settings.SettingsFragment.Companion.APP_LANGUAGE
import cat.covidcontact.tracker.feature.settings.SettingsFragment.Companion.ENABLE_DARK_THEME
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            currentFocus?.let { hideSoftKeyboard(it) }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun hideSoftKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE)
            as InputMethodManager

        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun attachBaseContext(base: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(base)
        val langCode = getLanguage(sharedPreferences) ?: Locale.getDefault().language
        super.attachBaseContext(base.updateBaseContextLocale(langCode))

        setUpUiMode(sharedPreferences)
    }

    private fun getLanguage(sharedPreferences: SharedPreferences): String? {
        return sharedPreferences.getString(APP_LANGUAGE, null)
    }

    private fun Context.updateBaseContextLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = resources.configuration
        configuration.setLocale(locale)
        return createConfigurationContext(configuration)
    }

    private fun setUpUiMode(sharedPreferences: SharedPreferences) {
        if (sharedPreferences.contains(ENABLE_DARK_THEME)) {
            enableDarkTheme(sharedPreferences.getBoolean(ENABLE_DARK_THEME, false))
        }
    }

    companion object {
        lateinit var instance: MainActivity
    }
}
