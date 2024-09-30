package com.flip.challenge.infrastructure.rest

import com.flip.challenge.domain.URLShortenerApplicationService
import com.flip.challenge.infrastructure.rest.model.CreateShortenedURLRequest
import com.flip.challenge.infrastructure.rest.model.CreateShortenedURLResponse
import com.flip.challenge.infrastructure.rest.model.GetInitialURLResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URL

@RestController
class TinyURLController(
    private val shortenerService: URLShortenerApplicationService
) {
    @PostMapping(
        value = ["/url"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun shorten(@RequestBody createShortenedURLRequest: CreateShortenedURLRequest): ResponseEntity<CreateShortenedURLResponse> {
        val tinyURL = shortenerService.shorten(createShortenedURLRequest)
        return ResponseEntity.ok(
            CreateShortenedURLResponse(
                shortCode = tinyURL.shortCode
            )
        )
    }

    @GetMapping(
        value = ["/{shortCode}"],
        produces = ["application/json"],
        consumes = ["application/json"],
    )
    fun getInitialURL(@PathVariable shortCode: String): ResponseEntity<GetInitialURLResponse> {
        val tinyURL = shortenerService.retrieveTinyURL(shortCode)

        return ResponseEntity.ok(
            GetInitialURLResponse(tinyURL.initialURL.toString())
        )
    }
}