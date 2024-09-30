package com.flip.challenge.domain

import java.net.URL

data class TinyURL(
    val shortCode: String,
    val initialURL: URL
)
