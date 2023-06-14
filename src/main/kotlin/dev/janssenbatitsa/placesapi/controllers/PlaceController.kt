package dev.janssenbatitsa.placesapi.controllers

import dev.janssenbatitsa.placesapi.models.Place
import dev.janssenbatitsa.placesapi.models.dtos.PlaceRequestDTO
import dev.janssenbatitsa.placesapi.services.PlaceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Create a place")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "201", description = "place created"),
                ApiResponse(responseCode = "409", description = "place with the same name already exists")
            ]
    )
    fun createPlace(@RequestBody @Valid placeRequestDTO: PlaceRequestDTO): ResponseEntity<Place> {
        val createdPlace = placeService.save(placeRequestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlace)
    }

    @PutMapping("/{placeId}", consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Update a place")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "place updated"),
                ApiResponse(responseCode = "404", description = "place with the id specified not found")
            ]
    )
    fun updatePlace(
            @PathVariable placeId: UUID,
            @RequestBody @Valid placeRequestDTO: PlaceRequestDTO
    ): ResponseEntity<Place> {
        val updatedPlace = placeService.update(placeId, placeRequestDTO)
        return ResponseEntity.status(HttpStatus.OK).body(updatedPlace)
    }

    @GetMapping("/{placeId}", produces = ["application/json"])
    @Operation(summary = "Get a place by id")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "place found"),
                ApiResponse(responseCode = "404", description = "place with the id specified not found")
            ]
    )
    fun getPlaceById(@PathVariable placeId: UUID): ResponseEntity<Place> {
        return ResponseEntity.ok(placeService.findPlaceById(placeId))
    }

    @GetMapping(produces = ["application/json"])
    @Operation(summary = "Get all places or places filtered by name")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "places found"),
            ]
    )
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
    @Operation(summary = "Delete a place by id")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "204", description = "place deleted"),
                ApiResponse(responseCode = "404", description = "place with the id specified not found")
            ]
    )
    fun deletePlace(@PathVariable placeId: UUID): ResponseEntity<Any> {
        placeService.deleteById(placeId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }


}