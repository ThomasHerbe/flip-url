package com.flip.challenge.domain

data class URLNotFound(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException()