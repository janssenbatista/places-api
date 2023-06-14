package dev.janssenbatitsa.placesapi

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(
        info = Info(title = "Places API", version = "1.0.0",
                contact = Contact(name = "Janssen Batista", email = "batistajanssen.dev@gmail.com")
        ))
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
