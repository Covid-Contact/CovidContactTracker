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
