package no.stunor.origo.organiserapi.model.courses

data class Map(
        var id: String? = null,
        var raceId: String,
        var scale: Double?,
        var mapPosition: MapPosition?,
        var controls: List<Control>,
        var courses: List<Course>
)