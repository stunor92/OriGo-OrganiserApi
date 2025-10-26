package no.stunor.origo.organiserapi.model.courses


data class Control (
    var type: ControlType?,
    var controlCode: String,
    var position: Int?,
    var mapPosition: Position
)
