package dev.janssenbatitsa.placesapi.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import dev.janssenbatitsa.placesapi.models.dtos.PlaceRequestDTO
import dev.janssenbatitsa.placesapi.repositories.PlaceRepository
import dev.janssenbatitsa.placesapi.services.PlaceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class PlaceControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var placeService: PlaceService

    @Autowired
    private lateinit var placeRepository: PlaceRepository

    @Autowired
    private lateinit var mapper: ObjectMapper

    private val placeRequestDTO = PlaceRequestDTO(
            name = "Place Name",
            city = "Place City",
            state = "Place State"
    )

    @AfterEach
    fun tearDown() {
        placeRepository.deleteAll()
    }

    // Create Place

    @Test
    fun `should be able to create a place and return status code 201`() {
        val dtoAsString = mapper.writeValueAsString(placeRequestDTO)
        mockMvc.perform(
                post(BASE_REQUEST_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(placeRequestDTO.name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slug").value("place-name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(placeRequestDTO.city))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(placeRequestDTO.state))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").isNotEmpty)
    }

    @Test
    fun `should not be able to create a place with an existing name and return status code 409`() {
        val createdPlace = placeRepository.save(placeRequestDTO.toPlace())
        val dtoAsString = mapper.writeValueAsString(placeRequestDTO)
        val response =
                mockMvc.perform(post(BASE_REQUEST_URI).contentType(MediaType.APPLICATION_JSON).content(dtoAsString))
                        .andExpect(MockMvcResultMatchers.status().isConflict).andReturn()
        assertThat(response.response.contentAsString)
                .isEqualTo("Place with name ${createdPlace.name} already exists!")
    }

    // Update place

    @Test
    fun `should be able to update a place and return status code 200`() {
        val createdPlace = placeRepository.save(placeRequestDTO.toPlace())
        val updatePlaceRequestDTO = PlaceRequestDTO(
                name = "New Place Name",
                city = "New Place City",
                state = "New Place State"
        )
        val dtoAsString = mapper.writeValueAsString(updatePlaceRequestDTO)
        mockMvc.perform(
                put("$BASE_REQUEST_URI/${createdPlace.id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdPlace.id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatePlaceRequestDTO.name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slug").value("new-place-name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(updatePlaceRequestDTO.city))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(updatePlaceRequestDTO.state))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").isNotEmpty)
    }

    @Test
    fun `should not be able to update a place with nonexistent id and return status code 404`() {
        val placeId = UUID.randomUUID()
        val updatePlaceRequestDTO = PlaceRequestDTO(
                name = "New Place Name",
                city = "New Place City",
                state = "New Place State"
        )
        val dtoAsString = mapper.writeValueAsString(updatePlaceRequestDTO)
        val response = mockMvc.perform(
                put("$BASE_REQUEST_URI/$placeId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dtoAsString)
        )
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andReturn()
        assertThat(response.response.contentAsString).isEqualTo("Place with id $placeId not found!")
    }

    // Find and filter places

    @Test
    fun `should be able to find a place by id and return status code 200`() {
        val createdPlace = placeRepository.save(placeRequestDTO.toPlace())
        mockMvc.perform(get("$BASE_REQUEST_URI/${createdPlace.id}"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdPlace.id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(createdPlace.name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.slug").value("place-name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(createdPlace.city))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value(createdPlace.state))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isNotEmpty)
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").isNotEmpty)
    }

    @Test
    fun `should not be able to find a place by id and return status code 404`() {
        val placeId = UUID.randomUUID()
        val response = mockMvc.perform(get("$BASE_REQUEST_URI/$placeId"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andReturn()
        assertThat(response.response.contentAsString).isEqualTo("Place with id $placeId not found!")
    }

    @Test
    fun `should find all saved places and return status code 200`() {
        val placeOne = PlaceRequestDTO(name = "Name one", city = "City one", state = "State one")
        val placeTwo = PlaceRequestDTO(name = "Name two", city = "City two", state = "State two")
        val places = placeRepository.saveAll(listOf(placeOne.toPlace(), placeTwo.toPlace()))
        mockMvc.perform(get(BASE_REQUEST_URI))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(places.size))
    }

    @Test
    fun `should find all saved places filtered by name and return status code 200`() {
        val placeOne = PlaceRequestDTO(name = "Name one", city = "City one", state = "State one")
        val placeTwo = PlaceRequestDTO(name = "Name two", city = "City two", state = "State two")
        val name = "one"
        placeRepository.saveAll(listOf(placeOne.toPlace(), placeTwo.toPlace()))
        mockMvc.perform(get("$BASE_REQUEST_URI?name=$name"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(1))
    }

    // Delete place

    @Test
    fun `should be able to delete a place and return status code 204`() {
        val createdPlace = placeRepository.save(placeRequestDTO.toPlace())
        mockMvc.perform(delete("$BASE_REQUEST_URI/${createdPlace.id}"))
                .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `should not be able to delete a place with nonexistent id and return status code 404`() {
        val placeId = UUID.randomUUID()
        val response = mockMvc.perform(delete("$BASE_REQUEST_URI/$placeId"))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
                .andReturn()
        assertThat(response.response.contentAsString).isEqualTo("Place with id $placeId not found!")
    }


    companion object {

        private const val BASE_REQUEST_URI = "/places"

        @Container
        @JvmStatic
        val container: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:14")
                .withUsername("postgres")
                .withPassword("postgres")

        @DynamicPropertySource
        @JvmStatic
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.username", container::getUsername)
            registry.add("spring.datasource.password", container::getPassword)
        }
    }
}