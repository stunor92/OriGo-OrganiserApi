package no.stunor.origo.organiserapi.model.entry

import com.fasterxml.jackson.annotation.JsonProperty
import no.stunor.origo.organiserapi.model.competitor.PunchingUnit
import no.stunor.origo.organiserapi.model.organisation.Organisation
import no.stunor.origo.organiserapi.model.person.Gender
import no.stunor.origo.organiserapi.model.person.PersonName
import java.sql.Timestamp

data class PersonEntry(
    @JsonProperty("entryId")
    var entryId: String,
    @JsonProperty("raceId")
    var raceId: String,
    @JsonProperty("name")
    var name: PersonName,
    @JsonProperty("competitorId")
    var competitorId: String? = null,
    @JsonProperty("personId")
    var personId: String? = null,
    @JsonProperty("organisation")
    var organisation: Organisation? = null,
    @JsonProperty("birthYear")
    var birthYear: Int? = null,
    @JsonProperty("nationality")
    var nationality: String? = null,
    @JsonProperty("gender")
    var gender: Gender = Gender.Other,
    @JsonProperty("classId")
    var classId: String,
    @JsonProperty("bib")
    var bib: String? = null,
    @JsonProperty("punchingUnits")
    var punchingUnits: List<PunchingUnit> = listOf(),
    @JsonProperty("status")
    var status: EntryStatus,
    @JsonProperty("startTime")
    var startTime: Timestamp? = null,
    @JsonProperty("finishTime")
    var finishTime: Timestamp? = null
)
