package no.stunor.origo.organiserapi.model.competitor

import no.stunor.origo.organiserapi.model.person.Gender
import no.stunor.origo.organiserapi.model.person.PersonName
import java.io.Serializable
import java.sql.Timestamp

data class TeamMemberCompetitor(
    var personId: String? = null,
    var name: PersonName? = null,
    var birthYear: Int? = null,
    var nationality: String? = null,
    var gender: Gender? = null,
    var punchingUnit: PunchingUnit? = null,
    var leg: Int = 1,
    var startTime: Timestamp? = null,
    var finishTime: Timestamp? = null,
    var legResult: Result? = null,
    var overallResult: Result? = null,
    var splitTimes: List<SplitTime> = listOf(),
    var entryFeeIds: List<String> = listOf()
)