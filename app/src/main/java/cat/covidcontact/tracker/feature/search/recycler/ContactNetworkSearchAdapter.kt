package cat.covidcontact.tracker.feature.search.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.NetworkState
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.managers.ColorManager
import cat.covidcontact.tracker.common.managers.TextManager
import cat.covidcontact.tracker.databinding.ContactNetworkSearchCardBinding

class ContactNetworkSearchAdapter(
    private val context: Context,
    private val colorManager: ColorManager<NetworkState>,
    private val textManager: TextManager<NetworkState>,
    private val onJoin: (ContactNetwork) -> Unit = {}
) : ListAdapter<ContactNetwork, ContactNetworkSearchAdapter.ContactNetworkSearchViewHolder>(
    ContactNetworkDiff
), ColorManager<NetworkState> by colorManager, TextManager<NetworkState> by textManager {

    inner class ContactNetworkSearchViewHolder(
        private val binding: ContactNetworkSearchCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun render(contactNetwork: ContactNetwork) {
            binding.bind(contactNetwork)
        }

        private fun ContactNetworkSearchCardBinding.bind(contactNetwork: ContactNetwork) {
            val (name, code) = contactNetwork.name.split("#")
            val displayCode = "#$code"
            val displayOwner = context.getString(R.string.owner_info, contactNetwork.owner.username)
            val cardColor = context.getColor(getColorFromValue(contactNetwork.networkState))
            val cardNetworkState = context.getString(getTextFromValue(contactNetwork.networkState))

            contactNetworkName.text = name
            contactNetworkCode.text = displayCode
            contactNetworkWithPassword.visibility =
                if (contactNetwork.isPasswordProtected) View.VISIBLE else View.GONE
            contactNetworkOwner.text = displayOwner
            contactNetworkState.text = cardNetworkState
            contactNetworkBorder.setBackgroundColor(cardColor)

            root.setOnClickListener { onJoin(contactNetwork) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactNetworkSearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ContactNetworkSearchCardBinding.inflate(layoutInflater, parent, false)
        return ContactNetworkSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactNetworkSearchViewHolder, position: Int) {
        holder.render(getItem(position))
    }

    object ContactNetworkDiff : DiffUtil.ItemCallback<ContactNetwork>() {
        override fun areItemsTheSame(oldItem: ContactNetwork, newItem: ContactNetwork): Boolean {
            return oldItem === newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ContactNetwork, newItem: ContactNetwork): Boolean {
            return oldItem == newItem
        }
    }
}
