package dev.cisnux.octobycisnux.ui

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import dagger.hilt.android.testing.HiltAndroidTest
import dev.cisnux.octobycisnux.R
import dev.cisnux.octobycisnux.launchFragmentInHiltContainer
import dev.cisnux.octobycisnux.ui.adapters.UsersAdapter
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest
class HomeFragmentTest {
    @Test
    fun test_navigation_to_detail_fragment() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        launchFragmentInHiltContainer<HomeFragment> {
            navController.setGraph(R.navigation.main_navigation)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.userRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<UsersAdapter.ViewHolder>(
                5,
                click()
            )
        )
        assertEquals(navController.currentDestination?.id, R.id.detailFragment)
    }
}
