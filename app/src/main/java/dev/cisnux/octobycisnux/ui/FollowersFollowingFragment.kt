package dev.cisnux.octobycisnux.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.octobycisnux.R
import dev.cisnux.octobycisnux.databinding.FragmentFollowersFollowingBinding
import dev.cisnux.octobycisnux.ui.adapters.UsersAdapter
import dev.cisnux.octobycisnux.utils.ApplicationStates
import dev.cisnux.octobycisnux.utils.SingleEvent
import dev.cisnux.octobycisnux.viewmodels.FollowersFollowingViewModel

@AndroidEntryPoint
class FollowersFollowingFragment : Fragment() {
    private val viewModel: FollowersFollowingViewModel by viewModels()
    private var position: Int = 0
    private var username: String? = null
    private var _binding: FragmentFollowersFollowingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get position and username arguments
        position = arguments?.getInt(ARG_POSITION) ?: position
        username = arguments?.getString(ARG_USERNAME)

        // to avoid multiple API calls
        if (savedInstanceState == null)
            username?.let(
                if (position == 0) viewModel::getFollowersByUsername
                else viewModel::getFollowingByUsername
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.followersFollowingStates.observe(
            viewLifecycleOwner,
            ::updateFollowerFollowingStates
        )
        // setup the UsersAdapter
        adapter = UsersAdapter { id, username ->
            val toDetailFragment =
                DetailFragmentDirections.actionDetailFragmentSelf(id, username)
            findNavController().navigate(toDetailFragment)
        }
        viewModel.followersFollowing.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.followersFollowing.observe(viewLifecycleOwner) {
            if (it.isEmpty())
                showNoFollowersOrFollowing(View.VISIBLE)
            else
                showNoFollowersOrFollowing(View.GONE)
        }
        // add adapter to recyclerView
        with(binding) {
            val layoutManager = LinearLayoutManager(requireActivity())
            val divider = DividerItemDecoration(requireActivity(), layoutManager.orientation)
            userRecyclerView.layoutManager = layoutManager
            userRecyclerView.addItemDecoration(divider)
            userRecyclerView.adapter = adapter
            userRecyclerView.setHasFixedSize(true)
        }
    }

    private fun updateFollowerFollowingStates(singleEvent: SingleEvent<ApplicationStates>) =
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
                binding.progressBar.visibility = View.VISIBLE
            }
            is ApplicationStates.Success -> {
                binding.progressBar.visibility = View.GONE
            }
        }

    private fun showNoFollowersOrFollowing(visibility: Int) = with(binding) {
        noFollowersFollowingIcon.visibility = visibility
        noFollowersFollowingTitle.visibility = visibility
        noFollowersFollowingMessage.visibility = visibility
        noFollowersFollowingTitle.text = getString(FOLLOWERS_OR_FOLLOWING_TITLES[position])
        noFollowersFollowingMessage.text = getString(FOLLOWERS_OR_FOLLOWING_MESSAGES[position])
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_POSITION: String = "arg_position"
        const val ARG_USERNAME: String = "arg_user"

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