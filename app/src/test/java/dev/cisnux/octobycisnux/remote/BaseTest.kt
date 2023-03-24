package dev.cisnux.octobycisnux.remote

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.io.FileInputStream

open class BaseTest {
    protected val mockWebServer = MockWebServer()

    fun mockResponse(fileName: String) {
        val inputStream = FileInputStream("/Users/cisnux/ApplicationDevelopments/OctobyCisnux/app/src/test/resources/$fileName")
        val buffer = inputStream.source().buffer()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(buffer.readString(Charsets.UTF_8))
        )

    }
}