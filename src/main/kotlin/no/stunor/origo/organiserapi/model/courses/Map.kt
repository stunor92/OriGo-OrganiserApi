package no.stunor.origo.organiserapi.model.courses

import com.google.cloud.firestore.annotation.DocumentId

data class Map(
        @DocumentId
        var id: String? = null,
        var raceId: String,
        var scale: Double?,
        var mapPosition: MapPosition?,
        var controls: List<Control>,
        var courses: List<Course>
)