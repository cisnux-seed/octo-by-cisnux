package dev.cisnux.octobycisnux.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.octobycisnux.databinding.FragmentFavoriteUserBinding
import dev.cisnux.octobycisnux.ui.adapters.UsersAdapter
import dev.cisnux.octobycisnux.viewmodels.FavoriteUserViewModel

@AndroidEntryPoint
class FavoriteUserFragment : Fragment() {
    private var _binding: FragmentFavoriteUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteUserViewModel by activityViewModels()
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setup the UsersAdapter
        adapter = UsersAdapter { id, username ->
            val toDetailFragment =
                FavoriteUserFragmentDirections.actionFavoriteUserFragmentToDetailFragment(
                    id,
                    username
                )
            findNavController().navigate(toDetailFragment)
        }
        viewModel.favoriteUsers.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.favoriteUsers.observe(viewLifecycleOwner) {
            showNoFavoriteUsers(if (it.isEmpty()) View.VISIBLE else View.GONE)
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

    private fun showNoFavoriteUsers(visibility: Int) = with(binding) {
        noFavoritesUsersImage.visibility = visibility
        noFavoriteUsersTitle.visibility = visibility
        noFavoriteUsersMessage.visibility = visibility
    }
}