package cat.covidcontact.tracker.feature.contactnetworks.recyclerview

import cat.covidcontact.model.NetworkState
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.managers.TextManager

class NetworkCardStateText : TextManager<NetworkState> {

    override fun getTextFromValue(value: NetworkState): Int {
        return when (value) {
            NetworkState.Visible -> R.string.network_visible
            NetworkState.Invisible -> R.string.network_invisible
            NetworkState.AlmostLimit -> R.string.network_almost_limit
            NetworkState.Limit -> R.string.network_limit
            NetworkState.OverLimit -> R.string.network_over_limit
            NetworkState.PositiveDetected -> R.string.network_positive_detected
        }
    }
}