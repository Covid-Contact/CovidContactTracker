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
import cat.covidcontact.tracker.common.managers.TextManager

class NetworkCardStateText : TextManager<NetworkState> {

    override fun getTextFromValue(value: NetworkState): Int {
        return when (value) {
            NetworkState.Normal -> R.string.network_normal
            NetworkState.AlmostLimit -> R.string.network_almost_limit
            NetworkState.Limit -> R.string.network_limit
            NetworkState.OverLimit -> R.string.network_over_limit
            NetworkState.PositiveDetected -> R.string.network_positive_detected
        }
    }
}
