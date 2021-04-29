package cat.covidcontact.tracker.feature.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.covidcontact.tracker.common.BaseFragment
import cat.covidcontact.tracker.common.extensions.observeList
import cat.covidcontact.tracker.common.handlers.ScreenStateHandler
import cat.covidcontact.tracker.databinding.FragmentSearchBinding
import cat.covidcontact.tracker.feature.contactnetworks.recyclerview.NetworkCardStateColor
import cat.covidcontact.tracker.feature.contactnetworks.recyclerview.NetworkCardStateText
import cat.covidcontact.tracker.feature.search.recycler.ContactNetworkSearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: ContactNetworkSearchAdapter

    override val viewModel: SearchViewModel by viewModels()
    override val screenStateHandler = ScreenStateHandler<SearchState> { context, state ->

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

        }

        contactNetworkSearchList.adapter = adapter
        contactNetworkSearchList.layoutManager = LinearLayoutManager(requireContext())
        contactNetworkSearchList.observeList(viewLifecycleOwner, viewModel.contactNetworks)
    }
}
