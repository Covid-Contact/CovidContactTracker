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