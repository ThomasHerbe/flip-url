package com.flip.challenge.infrastructure.jpa.entity

import com.flip.challenge.domain.TinyURL
import com.flip.challenge.domain.TinyURLRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.net.URL

@Repository
class JpaTinyURLRepository(
    private val springJpaTinyURLRepository: SpringJpaTinyURLRepository
) : TinyURLRepository {
    override fun save(url: TinyURL): TinyURL {
        springJpaTinyURLRepository.save(
            TinyURLEntity(
                shortCode = url.shortCode,
                initialUrl = url.initialURL.toString()
            )
        )

        return url
    }

    override fun get(shortCode: String): TinyURL? {
        return springJpaTinyURLRepository.findByShortCode(shortCode)?.let {
            TinyURL(
                shortCode = shortCode,
                initialURL = URL(it.initialUrl)
            )
        }
    }
}


interface SpringJpaTinyURLRepository : JpaRepository<TinyURLEntity, String> {
    fun findByShortCode(shortCode: String): TinyURLEntity?
}