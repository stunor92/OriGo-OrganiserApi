package no.stunor.origo.organiserapi.model.courses

import com.google.cloud.firestore.GeoPoint

data class Control (
    var type: ControlType?,
    var controlCode: String,
    var position: GeoPoint?,
    var mapPosition: Position
)
