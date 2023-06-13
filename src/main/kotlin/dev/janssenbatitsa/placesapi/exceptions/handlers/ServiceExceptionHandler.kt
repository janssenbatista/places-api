package dev.janssenbatitsa.placesapi.exceptions.handlers

import dev.janssenbatitsa.placesapi.exceptions.PlaceAlreadyExistsException
import dev.janssenbatitsa.placesapi.exceptions.PlaceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ServiceExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleException(ex: RuntimeException): ResponseEntity<Any> {
        return when (ex) {
            is PlaceNotFoundException -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
            is PlaceAlreadyExistsException -> ResponseEntity.status(HttpStatus.CONFLICT).body(ex.message)
            else -> ResponseEntity.badRequest().body(ex.message)
        }
    }
}