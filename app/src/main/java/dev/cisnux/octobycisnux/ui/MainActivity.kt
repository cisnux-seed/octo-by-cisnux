package dev.cisnux.octobycisnux.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.octobycisnux.R
import dev.cisnux.octobycisnux.databinding.ActivityMainBinding
import dev.cisnux.octobycisnux.viewmodels.HomeViewModel
import dev.cisnux.octobycisnux.viewmodels.SettingsViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navFragment.navController
        binding.navView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.settingsFragment,
                R.id.favoriteUserFragment,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        settingsViewModel.userThemePreferences.observe(this, ::setAppTheme)
        homeViewModel.hasFocus.observe(this, ::showBottomNavigation)
        navFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                    binding.navView.visibility = View.GONE
                }
                R.id.detailFragment -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    binding.navView.visibility = View.VISIBLE
                }
                else -> {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
                    binding.navView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showBottomNavigation(hasFocus: Boolean) {
        binding.navView.visibility = if (hasFocus) View.GONE else View.VISIBLE
    }

    private fun setAppTheme(isDarkModeActive: Boolean) = AppCompatDelegate.setDefaultNightMode(
        if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
    )
}