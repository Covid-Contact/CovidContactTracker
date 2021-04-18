package cat.covidcontact.tracker.feature.contactnetworks.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.NetworkState
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.managers.ColorManager
import cat.covidcontact.tracker.common.managers.TextManager
import cat.covidcontact.tracker.databinding.ContactNetworkCardBinding

class ContactNetworkAdapter(
    private val context: Context,
    private val currentUsername: String,
    private val colorManager: ColorManager<NetworkState>,
    private val textManager: TextManager<NetworkState>,
    private val onSettingsClick: () -> Unit = {}
) : ListAdapter<ContactNetwork, ContactNetworkAdapter.ContactNetworkViewHolder>(
    ContactNetworkDiff
), ColorManager<NetworkState> by colorManager, TextManager<NetworkState> by textManager {

    inner class ContactNetworkViewHolder(
        private val binding: ContactNetworkCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun render(contactNetwork: ContactNetwork) {
            binding.bind(contactNetwork)
        }

        private fun ContactNetworkCardBinding.bind(contactNetwork: ContactNetwork) {
            val (name, code) = contactNetwork.name.split("#")
            val displayCode = "#$code"
            val displayOwner = context.getString(R.string.owner_info, contactNetwork.owner.username)
            val cardColor = context.getColor(getColorFromValue(contactNetwork.networkState))
            val cardNetworkState = context.getString(getTextFromValue(contactNetwork.networkState))

            contactNetworkName.text = name
            contactNetworkCode.text = displayCode
            contactNetworkWithPassword.visibility =
                if (contactNetwork.password == null) View.GONE else View.VISIBLE
            contactNetworkOwner.text = displayOwner
            contactNetworkState.text = cardNetworkState
            contactNetworkBorder.setBackgroundColor(cardColor)
            contactNetworkConfiguration.visibility =
                if (contactNetwork.owner.username == currentUsername) View.VISIBLE else View.GONE
            contactNetworkConfiguration.setOnClickListener { onSettingsClick() }

            binding.root.setOnClickListener {
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())

                if (contactNetworkExpandable.visibility == View.GONE) {
                    contactNetworkExpandable.visibility = View.VISIBLE
                    contactNetworkExpandIcon.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_expand)
                    )
                } else {
                    contactNetworkExpandable.visibility = View.GONE
                    contactNetworkExpandIcon.setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_collapse)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactNetworkViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ContactNetworkCardBinding.inflate(layoutInflater, parent, false)
        return ContactNetworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactNetworkViewHolder, position: Int) {
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