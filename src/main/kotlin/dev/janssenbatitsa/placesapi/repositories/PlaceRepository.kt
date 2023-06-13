package dev.janssenbatitsa.placesapi.repositories

import dev.janssenbatitsa.placesapi.models.Place
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PlaceRepository : JpaRepository<Place, UUID> {

    fun existsByName(name: String): Boolean

    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Place>
}