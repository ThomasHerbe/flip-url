package com.flip.challenge.domain


interface TinyURLRepository {
    fun save(url: TinyURL): TinyURL
    fun get(shortCode: String): TinyURL?
}