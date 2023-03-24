package dev.cisnux.octobycisnux.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import coil.request.CachePolicy
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.octobycisnux.R
import dev.cisnux.octobycisnux.databinding.FragmentDetailBinding
import dev.cisnux.octobycisnux.domain.UserDetail
import dev.cisnux.octobycisnux.ui.adapters.SectionsPagerAdapter
import dev.cisnux.octobycisnux.utils.ApplicationStates
import dev.cisnux.octobycisnux.utils.SingleEvent
import dev.cisnux.octobycisnux.viewmodels.DetailViewModel

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private var username: String? = null
    private lateinit var adapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get username argument
        username = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        // to avoid multiple API calls
        if (savedInstanceState == null)
            username?.let(viewModel::getUserDetailByUsername)
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
        viewModel.userDetailStates.observe(viewLifecycleOwner, ::updateUserDetailStates)
        viewModel.userDetail.observe(viewLifecycleOwner, ::setUserProfile)
        viewModel.userDetail.observe(viewLifecycleOwner) { userDetail ->
            viewModel.isFavoriteUser(userDetail.id)
                .observe(viewLifecycleOwner, ::setFabFavoriteIcon)
        }
        viewModel.userDetail.observe(viewLifecycleOwner) { userDetail ->
            viewModel.isFavoriteUser(userDetail.id).observe(viewLifecycleOwner) { isFavoriteUser ->
                binding.fabFavoriteUser.setOnClickListener {
                    viewModel.updateFavoriteUser(
                        userDetail,
                        !isFavoriteUser
                    )
                }
            }
        }
        // set up a view pager and tab layout
        with(binding) {
            toolbar.inflateMenu(R.menu.detail_option_menu)
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            toolbar.setOnMenuItemClickListener(::setOptionMenu)
            adapter = SectionsPagerAdapter(
                requireActivity() as AppCompatActivity,
                this@DetailFragment.username
            )
            viewPager.adapter = adapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun setUserProfile(userDetail: UserDetail) =
        with(binding) {
            username.text = userDetail.username
            toolbar.title = userDetail.name
            followers.text = userDetail.totalFollowers.toString()
            following.text = userDetail.totalFollowing.toString()
            location.text = userDetail.location
            profilePict.load(userDetail.profilePict) {
                placeholder(R.drawable.avatar_loading_placeholder)
                crossfade(true)
                networkCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.ENABLED)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
        }

    @Suppress("DEPRECATION")
    private fun setOptionMenu(menu: MenuItem) = when (menu.itemId) {
        R.id.action_share -> {
            val intent = Intent().apply {
                type = "text/plain"
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "$USER_DETAIL_URL/$username"
                )
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            startActivity(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Intent.createChooser(intent, null)
                } else {
                    intent
                        .also {
                            requireActivity().packageManager?.resolveActivity(
                                intent,
                                PackageManager.MATCH_DEFAULT_ONLY
                            )
                        }
                })
            true
        }
        else -> false
    }

    private fun updateUserDetailStates(singleEvent: SingleEvent<ApplicationStates>) =
        when (val applicationStates = singleEvent.content) {
            is ApplicationStates.Failed -> {
                Toast.makeText(
                    requireActivity(),
                    applicationStates.error.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.progressBar.visibility = View.VISIBLE
            }
            is ApplicationStates.Loading -> binding.progressBar.visibility = View.VISIBLE
            is ApplicationStates.Success -> {
                binding.progressBar.visibility = View.GONE
            }
        }

    private fun setFabFavoriteIcon(isFavoriteUser: Boolean) =
        binding.fabFavoriteUser.setImageResource(
            if (isFavoriteUser)
                R.drawable.ic_favorite_24dp
            else R.drawable.ic_favorite_border_24dp
        )

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val USER_DETAIL_URL = "https://api.github.com/users"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
}