package dev.janssenbatitsa.placesapi.models.dtos

import dev.janssenbatitsa.placesapi.models.Place
import dev.janssenbatitsa.placesapi.utils.generateSlug
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class PlaceRequestDTO(
    @NotBlank(message = "Name cannot be blank")
    val name: String,
    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City must contain a maximum of 100 characters")
    val city: String,
    @NotBlank(message = "State cannot be blank")
    @Size(max = 50, message = "State must contain a maximum of 50 characters")
    val state: String
) {

    fun toPlace(): Place =
        Place(
            name = this.name,
            slug = this.name.generateSlug(),
            city = this.city,
            state = this.state,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
}
