package dev.cisnux.octobycisnux.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class UserServiceTest : BaseTest() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private var userService = Retrofit.Builder()
        .baseUrl(mockWebServer.url(BASE_URL))
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .client(client)
        .build()
        .create(UserService::class.java)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun get_user_detail() {
        // arrange
        mockResponse("user_detail.json")
        runBlocking(Dispatchers.IO) {
            // action
            val apiResponse = userService.getUserByUsername(USERNAME)

            // assert
            Assert.assertNotNull("API response wasn't null", apiResponse)
            Assert.assertEquals(
                "username didn't match",
                USERNAME,
                apiResponse.login
            )
        }
    }

    @Test
    fun get_users() {
        // arrange
        mockResponse("users.json")
        runBlocking(Dispatchers.IO) {
            // action
            val apiResponse = userService.getUsers(10)

            // assert
            Assert.assertEquals("API response is empty", false, apiResponse.isEmpty())
            Assert.assertEquals(
                "the responses wasn't contain 10 items",
                10,
                apiResponse.size
            )
        }
    }

    @Test
    fun get_user_by_username() {
        // arrange
        runBlocking(Dispatchers.IO) {
            // action
            mockResponse("searched_users.json")
            val apiResponse = userService.getUsersByUsername(USERNAME, PER_PAGE)
            val firstItem = apiResponse.items.first()

            // assert
            Assert.assertEquals("API response is empty", false, apiResponse.items.isEmpty())
            Assert.assertEquals(
                "the responses wasn't contain 10 items",
                SEARCH_PER_PAGE,
                apiResponse.items.size
            )
            Assert.assertEquals(
                "The searched username wasn't contain keyword", true, firstItem.login.contains(
                    SEARCH_USERNAME
                )
            )
        }
    }

    @Test
    fun get_user_followers() {
        // arrange
        runBlocking(Dispatchers.IO) {
            // action
            mockResponse("users.json")
            val apiResponse = userService.getFollowersByUsername(USERNAME, PER_PAGE)

            // assert
            Assert.assertEquals("API response is empty", false, apiResponse.isEmpty())
            Assert.assertEquals(
                "the responses wasn't contain 10 items",
                PER_PAGE,
                apiResponse.size
            )
        }
    }

    @Test
    fun get_users_following() {
        // arrange
        mockResponse("users.json")
        runBlocking(Dispatchers.IO) {
            // action
            val apiResponse = userService.getFollowingByUsername(USERNAME, PER_PAGE)

            // assert
            Assert.assertEquals("API response is empty", false, apiResponse.isEmpty())
            Assert.assertEquals(
                "the responses wasn't contain 10 items",
                PER_PAGE,
                apiResponse.size
            )
        }
    }

    companion object {
        private const val PER_PAGE = 10
        private const val SEARCH_PER_PAGE = 3
        private const val SEARCH_USERNAME = "khannedy"
        private const val USERNAME: String = "cisnux-seed"
        private const val BASE_URL = "/"
    }
}