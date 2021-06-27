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

package cat.covidcontact.tracker.feature.contactnetworks.recyclerview

import cat.covidcontact.model.NetworkState
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.managers.ColorManager

class NetworkCardStateColor : ColorManager<NetworkState> {
    override fun getColorFromValue(value: NetworkState): Int {
        return when (value) {
            NetworkState.Normal -> R.color.network_visible
            NetworkState.AlmostLimit -> R.color.network_almost_limit
            NetworkState.Limit -> R.color.network_limit
            NetworkState.OverLimit -> R.color.network_overlimit
            NetworkState.PositiveDetected -> R.color.network_positive_detected
        }
    }
}
