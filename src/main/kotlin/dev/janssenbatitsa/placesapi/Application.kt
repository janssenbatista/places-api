package dev.janssenbatitsa.placesapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PlacesApiApplication

fun main(args: Array<String>) {
	runApplication<PlacesApiApplication>(*args)
}
