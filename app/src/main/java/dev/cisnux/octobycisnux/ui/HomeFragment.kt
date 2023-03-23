package dev.cisnux.octobycisnux.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.octobycisnux.databinding.FragmentHomeBinding
import dev.cisnux.octobycisnux.ui.adapters.UsersAdapter
import dev.cisnux.octobycisnux.utils.ApplicationStates
import dev.cisnux.octobycisnux.utils.SingleEvent
import dev.cisnux.octobycisnux.viewmodels.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.usersStates.observe(viewLifecycleOwner, ::updateUsersStates)
        // set up a adapter
        adapter = UsersAdapter { id, username ->
            val toDetailFragment =
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(id, username)
            findNavController().navigate(toDetailFragment)
        }
        viewModel.users.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.users.observe(viewLifecycleOwner) {
            if (it.isEmpty())
                showNotFoundPlaceholder(View.VISIBLE)
            else showNotFoundPlaceholder(View.GONE)
        }

        // add a adapter to RecyclerView
        with(binding) {
            val layoutManager = LinearLayoutManager(requireActivity())
            val divider = DividerItemDecoration(requireActivity(), layoutManager.orientation)
            userRecyclerView.layoutManager = layoutManager
            userRecyclerView.addItemDecoration(divider)
            userRecyclerView.adapter = adapter
            userRecyclerView.setHasFixedSize(true)
            // add a listener for SearchView
            searchView.editText.setOnFocusChangeListener { _, hasFocus ->
                viewModel.hasFocus.value = hasFocus
            }
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    val query = searchView.text
                    if (query?.isNotEmpty() == true) {
                        viewModel.getUsersByUsername("$query")
                    }
                    searchBar.text = query
                    searchView.hide()
                    false
                }
        }
    }

    private fun updateUsersStates(singleEvent: SingleEvent<ApplicationStates>) =
        when (val applicationStates = singleEvent.content) {
            is ApplicationStates.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is ApplicationStates.Failed -> {
                singleEvent.getContentIfNotHandled()?.let {
                    Toast.makeText(
                        requireActivity(),
                        applicationStates.error.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                binding.progressBar.visibility = View.GONE
            }
            is ApplicationStates.Success -> {
                binding.progressBar.visibility = View.GONE
            }
        }


    private fun showNotFoundPlaceholder(visibility: Int) =
        with(binding.notFoundPlaceholder) {
            notFoundImage.visibility = visibility
            notFoundTitle.visibility = visibility
            notFoundMessage.visibility = visibility
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}