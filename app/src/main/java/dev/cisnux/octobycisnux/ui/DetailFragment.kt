package dev.cisnux.octobycisnux.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import dev.cisnux.octobycisnux.R
import dev.cisnux.octobycisnux.databinding.FragmentDetailBinding
import dev.cisnux.octobycisnux.utils.ApplicationNetworkStatus
import dev.cisnux.octobycisnux.viewmodels.DetailViewModel

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var argUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get username argument
        arguments?.let {
            argUsername = DetailFragmentArgs.fromBundle(it).username
        }
        // to avoid making multiple API calls when changing orientation
        if (savedInstanceState == null) {
            viewModel.getUserByUsername(argUsername)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeProgressRequest()
        setUserProfile()
        // set up  a view pager and tab layout
        with(binding) {
            topBar.setNavigationOnClickListener {
                view.findNavController().navigateUp()
            }
            val sectionsPagerAdapter =
                SectionsPagerAdapter(requireActivity() as AppCompatActivity).apply {
                    username = argUsername
                }
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun setUserProfile() {
        viewModel.user.observe(viewLifecycleOwner) { userDetail ->
            with(binding) {
                username.text = userDetail.username
                topBar.title = userDetail.name
                followers.text = userDetail.followers.toString()
                following.text = userDetail.following.toString()
                location.text = userDetail.location
                profilePict.load(userDetail.profilePict)
            }
        }
    }

    private fun subscribeProgressRequest() {
        viewModel.applicationNetworkStatus.observe(viewLifecycleOwner) {
            val networkStatus = it.content
            binding.progressBar.visibility = when (networkStatus) {
                is ApplicationNetworkStatus.Failed -> {
                    // single event for Toast
                    it.getContentIfNotHandled()?.let { _ ->
                        Toast.makeText(requireActivity(), networkStatus.message, Toast.LENGTH_SHORT)
                            .show()
                    }
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
}