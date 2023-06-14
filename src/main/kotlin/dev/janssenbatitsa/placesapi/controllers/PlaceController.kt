package dev.janssenbatitsa.placesapi.controllers

import dev.janssenbatitsa.placesapi.services.PlaceService
import dev.janssenbatitsa.placesapi.models.Place
import dev.janssenbatitsa.placesapi.models.dtos.PlaceRequestDTO
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/places")
class PlaceController(private val placeService: PlaceService) {

    @PostMapping
    fun createPlace(@RequestBody @Valid placeRequestDTO: PlaceRequestDTO): ResponseEntity<Place> {
        val createdPlace = placeService.save(placeRequestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlace)
    }

    @PutMapping("/{placeId}")
    fun updatePlace(
        @PathVariable placeId: UUID,
        @RequestBody @Valid placeRequestDTO: PlaceRequestDTO
    ): ResponseEntity<Place> {
        val updatedPlace = placeService.update(placeId, placeRequestDTO)
        return ResponseEntity.status(HttpStatus.OK).body(updatedPlace)
    }

    @GetMapping("/{placeId}")
    fun getPlaceById(@PathVariable placeId: UUID): ResponseEntity<Place> {
        return ResponseEntity.ok(placeService.findPlaceById(placeId))
    }

    @GetMapping
    fun getAllPlaces(
        @RequestParam(required = false) name: String?,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["name"],
            direction = Sort.Direction.ASC
        ) pageable: Pageable
    ): ResponseEntity<Page<Place>> {
        return if (name != null) {
            ResponseEntity.ok(placeService.findAllByName(name, pageable))
        } else {
            ResponseEntity.ok(placeService.findAllPlaces(pageable))
        }
    }

    @DeleteMapping("/{placeId}")
    fun deletePlace(@PathVariable placeId: UUID): ResponseEntity<Any> {
        placeService.deleteById(placeId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


}