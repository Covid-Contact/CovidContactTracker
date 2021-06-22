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

package cat.covidcontact.tracker.feature.userinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.common.BaseFragment
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
