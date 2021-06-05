package cat.covidcontact.tracker.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat
import cat.covidcontact.tracker.common.extensions.observeScreenState
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler

abstract class BaseSettingsFragment : PreferenceFragmentCompat() {
    abstract val preferenceResource: Int
    abstract val viewModel: BaseViewModel
    abstract val screenStateHandler: ScreenStateHandler<*>

    protected open fun onPreferencesCreated() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        screenStateHandler.context = requireActivity()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState.observeScreenState(viewLifecycleOwner, screenStateHandler)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onLoadNothing()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(preferenceResource, rootKey)
        onPreferencesCreated()
    }
}
