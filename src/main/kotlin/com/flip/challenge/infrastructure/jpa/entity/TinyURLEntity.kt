package com.flip.challenge.infrastructure.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity(name = "URL")
@Table(name = "url")
class TinyURLEntity(
    @Id
    @Column(name = "shortened_url")
    val shortCode: String,

    @Column(name = "initial_url", nullable = false)
    val initialUrl: String
)