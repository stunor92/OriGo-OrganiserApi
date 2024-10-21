package no.stunor.origo.organiserapi.model.competitor

import com.google.cloud.Timestamp
import java.io.Serializable


interface Competitor {
    var id: String?
    var name: Any
    var raceId: String
    var eventClassId: String
    var bib: String?
    var status: CompetitorStatus
    var startTime: Timestamp?
    var finishTime: Timestamp?
}