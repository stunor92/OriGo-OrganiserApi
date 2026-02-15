package no.stunor.origo.organiserapi.controller

import no.stunor.origo.organiserapi.services.EventEntryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event")
internal class EventController {

    @Autowired
    private lateinit var eventEntryService: EventEntryService

    /**
     * Syncs event entry list from eventor-api and saves signed up competitors to database
     * 
     * @param eventorId The eventor organization ID
     * @param eventId The event ID  
     * @param raceId The race ID
     * @return Response with the number of competitors synced
     */
    @PostMapping("/{eventorId}/{eventId}/{raceId}/sync-entries")
    fun syncEventEntries(
        @PathVariable("eventorId") eventorId: String,
        @PathVariable("eventId") eventId: String,
        @PathVariable("raceId") raceId: String
    ): ResponseEntity<Map<String, Any>> {
        val count = eventEntryService.syncEventEntryList(eventorId, eventId, raceId)
        
        return ResponseEntity(
            mapOf(
                "message" to "Successfully synced competitors",
                "count" to count,
                "eventorId" to eventorId,
                "eventId" to eventId,
                "raceId" to raceId
            ),
            HttpStatus.OK
        )
    }
}
