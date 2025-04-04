package no.stunor.origo.organiserapi.model.competitor

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.cloud.Timestamp
import io.swagger.v3.oas.annotations.media.Schema

@Schema(subTypes = [PersonCompetitor::class,TeamCompetitor::class])
interface Competitor {
    @set:Schema(description = "Unique identifier for the competitor. Only provide this if you are updating existing competitor.", required = false)
    var id: String?
    var name: Any
    var raceId: String
    var eventClassId: String
    var bib: String?
    var status: CompetitorStatus
    var startTime: Timestamp?
    var finishTime: Timestamp?
}