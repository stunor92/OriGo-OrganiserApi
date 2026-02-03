package no.stunor.origo.organiserapi.model.competitor

import io.swagger.v3.oas.annotations.media.Schema
import java.sql.Timestamp

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