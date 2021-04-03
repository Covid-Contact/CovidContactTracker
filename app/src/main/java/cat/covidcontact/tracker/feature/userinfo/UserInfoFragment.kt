package cat.covidcontact.tracker.feature.userinfo

import androidx.fragment.app.activityViewModels
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.navigate
import cat.covidcontact.tracker.common.extensions.navigateUp
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler

abstract class UserInfoFragment : BaseFragment() {
    override val viewModel: UserInfoViewModel by activityViewModels()
    override val screenStateHandler = ScreenStateHandler<UserInfoState> { context, state ->
        when (state) {
            is UserInfoState.LoadNextFragment -> navigate(state.navDirections)
            UserInfoState.LoadPreviousFragment -> navigateUp()
            is UserInfoState.UserInfoIntroduced -> navigate(state.navDirections)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onLoadNothing()
    }
}
