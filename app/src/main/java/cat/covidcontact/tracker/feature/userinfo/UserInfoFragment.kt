package cat.covidcontact.tracker.feature.userinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.navigate
import cat.covidcontact.tracker.common.extensions.navigateUp
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.feature.userinfo.fragments.CovidInformationFragmentDirections

abstract class UserInfoFragment : BaseFragment() {
    override val viewModel: UserInfoViewModel by activityViewModels()
    override val screenStateHandler = ScreenStateHandler<UserInfoState> { context, state ->
        when (state) {
            is UserInfoState.LoadNextFragment -> navigate(state.navDirections)
            UserInfoState.LoadPreviousFragment -> navigateUp()
            is UserInfoState.UserInfoIntroduced -> navigate(state.navDirections)
            is UserInfoState.UserInfoCreated -> {
                val action = CovidInformationFragmentDirections
                    .actionCovidInformationFragmentToMainFragment(state.email)
                navigate(action)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentUser?.let { setUpExistingData(it) }
    }

    abstract fun setUpExistingData(user: User)
}
