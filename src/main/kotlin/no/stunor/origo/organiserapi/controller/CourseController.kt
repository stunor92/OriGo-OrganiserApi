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
import org.iof.eventor.CourseData
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
internal class CourseController {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostMapping("/eventor/{eventorId}/event/{eventId}/race/{raceId}/courses")
    fun postResult(
        @PathVariable(value = "eventorId") eventorId: String,
        @PathVariable(value = "eventId") eventId: String,
        @PathVariable(value = "raceId") raceId: String,
        @RequestBody(required = true) record: CourseData
    ) {
        //TODO
    }

}