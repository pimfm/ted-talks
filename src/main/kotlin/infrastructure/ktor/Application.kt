package infrastructure.ktor

import com.iodigital.infrastructure.configureRouting
import com.iodigital.infrastructure.configureSerialization
import infrastructure.database.TalksRepositoryUsingCSV
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8989, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
        .addShutdownHook { /* saveTalksToCSVOnShutdownOfApplication() */ }
}

fun Application.module() {
    configureSerialization()

    val csv = File("src/main/kotlin/ted-talks.csv")

    with (TalksRepositoryUsingCSV(csv)) {
        configureRouting()
    }
}
