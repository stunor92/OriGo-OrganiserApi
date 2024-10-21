package no.stunor.origo.organiserapi.model.emit

import no.stunor.origo.organiserapi.model.competitor.PunchingUnit
import no.stunor.origo.organiserapi.model.competitor.SplitTime

data class EmitRecord(
        var punchingUnit: PunchingUnit,
        var splitTimes: List<SplitTime> = listOf(),

)