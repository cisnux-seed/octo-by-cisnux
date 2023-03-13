package dev.cisnux.octobycisnux.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.cisnux.octobycisnux.R
import dev.cisnux.octobycisnux.databinding.FragmentFollowBinding
import dev.cisnux.octobycisnux.utils.ApplicationNetworkStatus
import dev.cisnux.octobycisnux.viewmodels.FollowViewModel

class FollowFragment : Fragment() {
    private val viewModel: FollowViewModel by viewModels()
    private var position = 0
    private lateinit var username: String
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get position and username arguments
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME, "")
        }
        // to avoid making multiple API calls when changing orientation
        if (savedInstanceState == null) {
            viewModel.getFollowersOrFollowingByUsername(position, username)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun subscribeProgressRequest() =
        viewModel.applicationNetworkStatus.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = when (val networkStatus = it.content) {
                is ApplicationNetworkStatus.Failed -> {
                    // single event for Toast
                    it.getContentIfNotHandled()?.let { _ ->
                        Toast.makeText(requireActivity(), networkStatus.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    showNoFollowersOrFollowing(View.GONE)
                    View.VISIBLE
                }
                is ApplicationNetworkStatus.Success -> {
                    if (networkStatus.isEmpty)
                        showNoFollowersOrFollowing(View.VISIBLE)
                    else showNoFollowersOrFollowing(View.GONE)
                    View.GONE
                }
                else -> {
                    showNoFollowersOrFollowing(View.GONE)
                    View.VISIBLE
                }
            }
        }

    private fun showNoFollowersOrFollowing(visibility: Int): Unit = with(binding) {
        noFollowersIcon.visibility = visibility
        noFollowersTitle.visibility = visibility
        noFollowersMessage.visibility = visibility
        noFollowersTitle.text = getString(FOLLOWERS_OR_FOLLOWING_TITLES[position])
        noFollowersMessage.text = getString(FOLLOWERS_OR_FOLLOWING_MESSAGES[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_USERNAME = "arg_username"
        const val ARG_POSITION = "arg_position"

        @StringRes
        private val FOLLOWERS_OR_FOLLOWING_TITLES = intArrayOf(
            R.string.no_followers_title,
            R.string.no_following_title
        )

        @StringRes
        private val FOLLOWERS_OR_FOLLOWING_MESSAGES = intArrayOf(
            R.string.no_followers_message,
            R.string.no_following_message
        )
    }
}