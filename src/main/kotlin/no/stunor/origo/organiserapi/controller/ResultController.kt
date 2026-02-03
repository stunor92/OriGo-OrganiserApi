package no.stunor.origo.organiserapi.controller

import no.stunor.origo.organiserapi.model.competitor.*
import no.stunor.origo.organiserapi.model.emit.EmitRecord
import no.stunor.origo.organiserapi.model.organisation.Organisation
import no.stunor.origo.organiserapi.model.organisation.OrganisationType
import no.stunor.origo.organiserapi.model.person.Gender
import no.stunor.origo.organiserapi.model.person.PersonName
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
internal class ResultController {

    @PostMapping("/result/{eventorId}/{eventId}/{raceId}")
    fun postResult(
        @PathVariable(value = "eventorId") eventorId: String,
        @PathVariable(value = "eventId") eventId: String,
        @PathVariable(value = "raceId") raceId: String,
        @RequestBody(required = true) record: EmitRecord
    ): ResponseEntity<Competitor> {
        //TODO
        val competitor = PersonCompetitor(
            id = "1234-5678-91011",
            personId = "123",
            eventClassId = "123",
            name = PersonName(
                given = "John",
                family = "Doe"
            ),
            organisation = Organisation(
                organisationId = "141",
                name = "IL Gneist",
                type = OrganisationType.Club,
                country = "NOR"
            ),
            birthYear = 1992,
            nationality = "NOR",
            gender = Gender.Man,
            punchingUnit = PunchingUnit(
                id = "123456",
                type = PunchingUnitType.Emit
            ),
            bib = null,
            status = CompetitorStatus.Finished,
            startTime = null,
            finishTime = null,
            result = Result(
                time = 1234,
                timeBehind = 0,
                position = 1,
                status = ResultStatus.OK
            ),
            splitTimes = record.splitTimes,
            entryFeeIds = listOf("1234", "5678")

        )
        return ResponseEntity(competitor, HttpStatus.OK)
    }
}