package com.flip.challenge.domain

import com.flip.challenge.infrastructure.rest.model.CreateShortenedURLRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URL
import java.util.Random

@Service
class URLShortenerApplicationService(
    private val tinyURLRepository: TinyURLRepository
) {
    private val logger = KotlinLogging.logger { }

    private companion object {
        const val CODE_LENGTH = 10
    }

    @Transactional
    fun shorten(request: CreateShortenedURLRequest): TinyURL {
        logger.info { "Create a new short code" }
        return tinyURLRepository.save(
            TinyURL(
                shortCode = generateShortCode(),
                initialURL = URL(request.initialURL)
            )
        )
    }

    @Transactional(readOnly = true)
    fun retrieveTinyURL(shortCode: String): TinyURL {
        logger.info { "Retrieving URL with code: $shortCode" }
        return tinyURLRepository.get(shortCode)
            ?: throw URLNotFound("No URL could be found with the code: $shortCode")
    }

    private fun generateShortCode(): String {
        val rand = Random()
        val sb = StringBuilder()

        repeat(CODE_LENGTH) {
            val isLetter = rand.nextBoolean()
            if (isLetter) {
                val nextLetter = 'a' + rand.nextInt(26)
                val isUpperCase = rand.nextBoolean()
                sb.append(
                    if (isUpperCase) nextLetter.uppercase()
                    else nextLetter
                )
            } else {
                sb.append(('0' + rand.nextInt(10)))
            }
        }

        return sb.toString()
    }
}