package no.stunor.origo.organiserapi.model.courses

import jakarta.persistence.Column

data class GeoPosition (
    @Column(name = "geo_lat")
    var latitude: Double = 0.0,
    @Column(name = "geo_lng")
    var longitude: Double = 0.0
)
