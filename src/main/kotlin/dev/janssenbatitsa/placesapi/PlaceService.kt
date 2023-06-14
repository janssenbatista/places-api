package dev.janssenbatitsa.placesapi

import dev.janssenbatitsa.placesapi.exceptions.PlaceAlreadyExistsException
import dev.janssenbatitsa.placesapi.exceptions.PlaceNotFoundException
import dev.janssenbatitsa.placesapi.models.Place
import dev.janssenbatitsa.placesapi.models.dtos.PlaceRequestDTO
import dev.janssenbatitsa.placesapi.repositories.PlaceRepository
import dev.janssenbatitsa.placesapi.utils.generateSlug
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class PlaceService(private val placeRepository: PlaceRepository) {

    fun save(placeRequestDTO: PlaceRequestDTO): Place {
        if (placeRepository.existsByName(placeRequestDTO.name)) {
            throw PlaceAlreadyExistsException("Place with name ${placeRequestDTO.name} already exists!")
        }
        return placeRepository.save(placeRequestDTO.toPlace())
    }

    fun update(placeId: UUID, placeRequestDTO: PlaceRequestDTO): Place {
        val place = placeRepository.findById(placeId).orElseThrow {
            PlaceNotFoundException("Place with id $placeId not found")
        }
        place.apply {
            name = placeRequestDTO.name
            slug = placeRequestDTO.name.generateSlug()
            state = placeRequestDTO.state
            city = placeRequestDTO.city
            updatedAt = LocalDateTime.now()
        }
        return placeRepository.save(place)
    }

    fun findPlaceById(placeId: UUID): Place {
        return placeRepository.findById(placeId).orElseThrow {
            PlaceNotFoundException("Place with id $placeId not found")
        }
    }

    fun findAllPlaces(pageable: Pageable): Page<Place> = placeRepository.findAll(pageable)

    fun findAllByName(name: String, pageable: Pageable): Page<Place> =
        placeRepository.findByNameContainingIgnoreCase(name, pageable)

    fun deleteById(placeId: UUID) {
        val place = placeRepository.findById(placeId).orElseThrow {
            PlaceNotFoundException("Place with id $placeId not found")
        }
        placeRepository.deleteById(place.id)
    }
}