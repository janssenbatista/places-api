package dev.janssenbatitsa.placesapi.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "tb_places")
data class Place(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column(unique = true)
    var name: String,
    @Column(unique = true)
    var slug: String,
    @Column(length = 100)
    var city: String,
    @Column(length = 50)
    var state: String,
    @Column(name = "created_at")
    val createdAt: LocalDateTime,
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime
)
