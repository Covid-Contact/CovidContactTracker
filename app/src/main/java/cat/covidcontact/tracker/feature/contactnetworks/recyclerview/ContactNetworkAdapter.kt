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
import cat.covidcontact.tracker.databinding.ContactNetworkCardBinding

class ContactNetworkAdapter(
    private val context: Context,
    private val currentUsername: String,
    private val colorManager: ColorManager<NetworkState>,
    private val textManager: TextManager<NetworkState>,
    private val onSettingsClick: (ContactNetwork) -> Unit = {},
    private val onExitContactNetwork: (ContactNetwork) -> Unit = {}
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
            val isCurrentUserOwner = contactNetwork.owner.username == currentUsername

            contactNetworkName.text = name
            contactNetworkCode.text = displayCode
            contactNetworkWithPassword.visibility =
                if (contactNetwork.isPasswordProtected) View.VISIBLE else View.GONE
            contactNetworkOwner.text = displayOwner
            contactNetworkState.text = cardNetworkState
            contactNetworkBorder.setBackgroundColor(cardColor)
            contactNetworkConfiguration.visibility =
                if (isCurrentUserOwner) View.VISIBLE else View.GONE
            contactNetworkQuit.visibility =
                if (!isCurrentUserOwner) View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                if (isCurrentUserOwner) {
                    onSettingsClick(contactNetwork)
                } else {
                    onExitContactNetwork(contactNetwork)
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
