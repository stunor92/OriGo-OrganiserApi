package no.stunor.origo.organiserapi.model.competitor

data class PunchingUnit (
        var id: String = "",
        var type: PunchingUnitType = PunchingUnitType.Other
)