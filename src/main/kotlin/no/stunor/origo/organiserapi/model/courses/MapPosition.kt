package no.stunor.origo.organiserapi.model.courses

import jakarta.persistence.Embeddable
import jakarta.persistence.Column

@Embeddable
data class MapPosition(
    @Column(name = "map_x")
    var x: Double = 0.0,
    @Column(name = "map_y")
    var y: Double = 0.0
)
