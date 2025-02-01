package com.iodigital.infrastructure

import domain.contracts.TalksRepository
import domain.talks.TedTalk
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

context(TalksRepository)
fun Application.configureRouting() {
    routing {
        openAPI(path = "openapi")
        get("/talks") {
            val talks = getTalks(
                pageNumber = call.parameters["pageNumber"]?.toInt() ?: 1,
                countPerPage = call.parameters["countPerPage"]?.toInt() ?: 100
            )
            call.respond(talks)
        }
        get("/talks/search/{query}") {
            val talks = searchTalks(
                searchTerm = call.parameters["query"] ?: "",
                count = call.parameters["count"]?.toInt() ?: 100
            )
            call.respond(talks)
        }
        post("/talks") {
            addTalk(
                talk = call.receive<TedTalk>()
            )

            call.respond(HttpStatusCode.NoContent)
        }
        get("/talks/most-influential") {
            call.respond(
                getInfluentialTalksByYear()
            )
        }
    }
}
