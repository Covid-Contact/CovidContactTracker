package cat.covidcontact.tracker.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.*
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentSearchBinding
import cat.covidcontact.tracker.feature.contactnetworks.recyclerview.NetworkCardStateColor
import cat.covidcontact.tracker.feature.contactnetworks.recyclerview.NetworkCardStateText
import cat.covidcontact.tracker.feature.main.MainViewModel
import cat.covidcontact.tracker.feature.search.recycler.ContactNetworkSearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: ContactNetworkSearchAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    override val viewModel: SearchViewModel by viewModels()
    override val screenStateHandler = ScreenStateHandler<SearchState> { context, state ->
        when (state) {
            is SearchState.ContactNetworkJoined -> {
                binding.txtInputAccessCode.clear()
                context.showDialog(
                    title = context.getString(R.string.contact_network_joined_title),
                    message = context.getString(
                        R.string.contact_network_joined_message,
                        state.contactNetworkName
                    )
                )
            }
            is SearchState.AlreadyJoined -> {
                binding.txtInputAccessCode.clear()
                context.showDialog(
                    title = context.getString(R.string.contact_network_already_joined_title),
                    message = context.getString(
                        R.string.contact_network_already_joined_message,
                        state.contactNetworkName
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bind()
    }

    private fun FragmentSearchBinding.bind() {
        adapter = ContactNetworkSearchAdapter(
            requireContext(),
            NetworkCardStateColor(),
            NetworkCardStateText()
        ) { contactNetwork ->
            val user = mainViewModel.userDevice.requireValue().user
            viewModel.onJoinContactNetwork(user, contactNetwork)
        }

        contactNetworkSearchList.adapter = adapter
        contactNetworkSearchList.layoutManager = LinearLayoutManager(requireContext())
        contactNetworkSearchList.observeList(viewLifecycleOwner, viewModel.contactNetworks)

        txtInputAccessCode.observeText(viewModel.accessCode)
        txtInputAccessCode.observeEndIconActivated(
            viewLifecycleOwner,
            viewModel.isSearchByAccessCodeEnabled
        )
        txtInputAccessCode.setEndIconOnClickListener {
            viewModel.onGetContactNetworkByAccessCode()
        }
    }
}
