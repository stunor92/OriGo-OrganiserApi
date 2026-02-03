package no.stunor.origo.organiserapi.model.courses

import java.util.*

data class Control (
    var id: UUID? = null,
    var type: ControlType?,
    var controlCode: String,
    var mapPosition: MapPosition,
    var geoPosition: GeoPosition?,
    var mapId: UUID? = null
) {
    override fun toString(): String = "Control(controlCode='$controlCode', type=$type)"
}

