package cat.covidcontact.tracker.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import cat.covidcontact.tracker.common.extensions.observeScreenState
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler

abstract class BaseFragment : Fragment() {
    abstract val screenStateHandler: ScreenStateHandler<*>
    abstract val viewModel: BaseViewModel

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

    protected fun navigate(
        action: NavDirections,
        navController: NavController = findNavController()
    ) {
        navController.navigate(action)
        viewModel.onLoadNothing()
    }

    protected fun navigateUp(navController: NavController = findNavController()) {
        navController.navigateUp()
        viewModel.onLoadNothing()
    }
}
