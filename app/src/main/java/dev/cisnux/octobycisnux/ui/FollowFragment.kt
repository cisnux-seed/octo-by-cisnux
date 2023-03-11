package dev.cisnux.octobycisnux.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.cisnux.octobycisnux.databinding.FragmentFollowBinding
import dev.cisnux.octobycisnux.utils.ApplicationNetworkStatus
import dev.cisnux.octobycisnux.viewmodels.FollowViewModel

class FollowFragment : Fragment() {
    private val viewModel: FollowViewModel by viewModels()
    private var position = 1
    private lateinit var username: String
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // get position and username arguments
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME, "")
        }
        viewModel.getFollowersOrFollowingByUsername(position, username)
        subscribeProgressRequest()

        // setup the UsersAdapter
        val adapter = UsersAdapter { username ->
            val toDetailFragment =
                DetailFragmentDirections.actionDetailFragmentSelf(username)
            view.findNavController().navigate(toDetailFragment)
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        val divider = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        viewModel.userFollows.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // add adapter to recyclerView
        with(binding) {
            userRecyclerView.layoutManager = layoutManager
            userRecyclerView.addItemDecoration(divider)
            userRecyclerView.adapter = adapter
            userRecyclerView.setHasFixedSize(true)
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

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_POSITION = "arg_position"
    }
}