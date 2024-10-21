package no.stunor.origo.organiserapi.controller

import com.google.cloud.Timestamp
import no.stunor.origo.organiserapi.model.competitor.Competitor
import no.stunor.origo.organiserapi.model.competitor.CompetitorStatus
import no.stunor.origo.organiserapi.model.competitor.PersonCompetitor
import no.stunor.origo.organiserapi.model.competitor.PunchingUnit
import no.stunor.origo.organiserapi.model.competitor.PunchingUnitType
import no.stunor.origo.organiserapi.model.competitor.Result
import no.stunor.origo.organiserapi.model.competitor.ResultStatus
import no.stunor.origo.organiserapi.model.emit.EmitRecord
import no.stunor.origo.organiserapi.model.organisation.Organisation
import no.stunor.origo.organiserapi.model.organisation.OrganisationType
import no.stunor.origo.organiserapi.model.person.Gender
import no.stunor.origo.organiserapi.model.person.PersonName
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
internal class Controller {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/eventor/{eventorId}/event/{eventId}/race/{raceId}/result")
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
            startTime = Timestamp.now(),
            finishTime = Timestamp.now(),
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
    @PostMapping("/eventor/{eventorId}/event/{eventId}/race/{raceId}/competitor")
    fun postCompetitor(
        @PathVariable(value = "eventorId") eventorId: String,
        @PathVariable(value = "eventId") eventId: String,
        @PathVariable(value = "raceId") raceId: String,
        @RequestBody(required = true) competitor: Competitor
    ) {
        //TODO
    }

    @GetMapping("/eventor/{eventorId}/event/{eventId}/race/{raceId}/competitor")
    fun getCompetitor(
        @PathVariable(value = "eventorId") eventorId: String,
        @PathVariable(value = "eventId") eventId: String,
        @PathVariable(value = "raceId") raceId: String,
        @RequestParam(value = "punchingUnitId") punchingUnitId: String

    ) : ResponseEntity<Competitor> {
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
            startTime = Timestamp.now(),
            finishTime = Timestamp.now(),
            result = Result(
                time = 1234,
                timeBehind = 0,
                position = 1,
                status = ResultStatus.OK
            ),
            splitTimes = listOf(),
            entryFeeIds = listOf("1234", "5678")

        )
        return ResponseEntity(competitor, HttpStatus.OK)

    }
}