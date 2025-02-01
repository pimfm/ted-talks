package com.iodigital.infrastructure

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(Compression)
    install(ContentNegotiation) {
        json()
    }
}
