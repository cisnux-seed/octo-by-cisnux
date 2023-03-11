package dev.cisnux.octobycisnux.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.cisnux.octobycisnux.databinding.FragmentHomeBinding
import dev.cisnux.octobycisnux.utils.ApplicationNetworkStatus
import dev.cisnux.octobycisnux.viewmodels.HomeViewModel


class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeProgressRequest()

        // set up a adapter
        val adapter = UsersAdapter { username ->
            val toDetailFragment =
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(username)
            view.findNavController().navigate(toDetailFragment)
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        val divider = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        viewModel.users.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // add adapter to RecyclerView
        with(binding) {
            userRecyclerView.layoutManager = layoutManager
            userRecyclerView.addItemDecoration(divider)
            userRecyclerView.adapter = adapter
            userRecyclerView.setHasFixedSize(true)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = searchView.text
                if (query?.isEmpty() != true) {
                    viewModel.getUsersByUsername(query.toString())
                }
                searchBar.text = query
                searchView.hide()
                false
            }
        }
    }

    private fun subscribeProgressRequest() {
        viewModel.applicationNetworkStatus.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = when (it) {
                is ApplicationNetworkStatus.Failed -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    View.VISIBLE
                }
                is ApplicationNetworkStatus.Success -> View.GONE
                else -> View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}