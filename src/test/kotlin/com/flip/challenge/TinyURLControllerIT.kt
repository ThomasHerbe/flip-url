package com.flip.challenge

import com.fasterxml.jackson.databind.ObjectMapper
import com.flip.challenge.config.TestcontainersConfiguration
import com.flip.challenge.infrastructure.rest.model.CreateShortenedURLRequest
import com.flip.challenge.infrastructure.rest.model.CreateShortenedURLResponse
import com.flip.challenge.infrastructure.rest.model.GetInitialURLResponse
import com.github.dockerjava.zerodep.shaded.org.apache.commons.codec.CharEncoding
import jakarta.annotation.PostConstruct
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@SpringBootTest(
    classes = [
        TestcontainersConfiguration::class
    ]
)
@ActiveProfiles("it")
class TinyURLControllerIT {
    @Autowired
    private lateinit var wac: WebApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    lateinit var mockMvc: MockMvc

    private companion object {
        private const val FLIP_URL = "https://www.getflip.com/about-us/"
    }

    @PostConstruct
    fun postConstruct() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @Nested
    inner class Shorten {
        @Test
        fun `it returns a random code with 10 characters`() {
            val response = shorten(CreateShortenedURLRequest(FLIP_URL))
                .andExpect { status { isOk() } }
                .to(CreateShortenedURLResponse::class.java)

            assertEquals(10, response.shortCode.length)
        }

        @Test
        fun `it accepts the same URL multiple times and does not return the same code every time`() {
            val firstGeneration = shorten(CreateShortenedURLRequest(FLIP_URL))
                .andExpect { status { isOk() } }.to(CreateShortenedURLResponse::class.java)

            val secondGeneration = shorten(CreateShortenedURLRequest(FLIP_URL))
                .andExpect { status { isOk() } }.to(CreateShortenedURLResponse::class.java)

            assertNotEquals(firstGeneration.shortCode, secondGeneration.shortCode)
        }
    }

    @Nested
    inner class GetInitialURL {
        @Test
        fun `it returns the initial URL when it matches an existing code`() {
            val response = shorten(CreateShortenedURLRequest(FLIP_URL))
                .andExpect { status { isOk() } }
                .to(CreateShortenedURLResponse::class.java)

            val initialURLResponse = getInitialURL(response.shortCode)
                .andExpect { status { isOk() } }
                .to(GetInitialURLResponse::class.java)

            assertEquals(FLIP_URL, initialURLResponse.initialURL)
        }

        @Test
        fun `it returns a 404 if the code does not exist`() {
            getInitialURL("unknown")
                .andExpect { status { isNotFound() } }
        }
    }

    private fun shorten(request: CreateShortenedURLRequest): ResultActionsDsl {
        return mockMvc.post("/url") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            characterEncoding = CharEncoding.UTF_8
            content = objectMapper.writeValueAsString(request)
        }
    }

    private fun getInitialURL(shortCode: String): ResultActionsDsl {
        return mockMvc.get("/{shortCode}", shortCode) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            characterEncoding = CharEncoding.UTF_8
        }
    }

    private fun <T> ResultActionsDsl.to(clazz: Class<T>): T {
        return andReturn().response.contentAsString.let {
            objectMapper.readValue(it, clazz)
        }
    }
}