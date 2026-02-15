package no.stunor.origo.organiserapi.services

import no.stunor.origo.organiserapi.data.CompetitorRepository
import no.stunor.origo.organiserapi.model.competitor.CompetitorStatus
import no.stunor.origo.organiserapi.model.competitor.PersonCompetitor
import no.stunor.origo.organiserapi.model.entry.EntryStatus
import no.stunor.origo.organiserapi.model.entry.PersonEntry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Service
open class EventEntryService {

    @Autowired
    private lateinit var competitorRepository: CompetitorRepository

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Value("\${eventor.api.base-url:}")
    private lateinit var eventorApiBaseUrl: String

    private val logger = LoggerFactory.getLogger(EventEntryService::class.java)

    /**
     * Fetches event entry list from eventor-api and saves signed up competitors to database
     * 
     * @param eventorId The eventor organization ID
     * @param eventId The event ID
     * @param raceId The race ID
     * @return Number of competitors saved
     */
    @Transactional
    open fun syncEventEntryList(eventorId: String, eventId: String, raceId: String): Int {
        logger.info("Syncing event entry list for eventorId: {}, eventId: {}, raceId: {}", eventorId, eventId, raceId)
        
        // Fetch entries from eventor-api
        val entries = fetchEventEntryList(eventorId, eventId)
        
        // Filter entries for the specific race and convert to PersonCompetitor
        val competitors = entries
            .filter { it.raceId == raceId }
            .map { convertToPersonCompetitor(it, raceId) }
        
        logger.info("Found {} competitors for race {}", competitors.size, raceId)
        
        // Save to database
        competitors.forEach { competitor ->
            try {
                competitorRepository.save(competitor)
                logger.debug("Saved competitor: {} {}", 
                    competitor.name, 
                    competitor.personId)
            } catch (e: Exception) {
                logger.error("Error saving competitor: {} {}", 
                    competitor.name, 
                    competitor.personId, 
                    e)
            }
        }
        
        logger.info("Successfully synced {} competitors", competitors.size)
        return competitors.size
    }

    /**
     * Fetches event entry list from the eventor-api
     */
    private fun fetchEventEntryList(eventorId: String, eventId: String): List<PersonEntry> {
        val url = "$eventorApiBaseUrl/event/$eventorId/$eventId/entry-list"
        
        logger.debug("Fetching entries from: {}", url)
        
        return try {
            val response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<PersonEntry>>() {}
            )
            
            response.body ?: emptyList()
        } catch (e: Exception) {
            logger.error("Error fetching entry list from eventor-api: {}", e.message, e)
            emptyList()
        }
    }

    /**
     * Converts PersonEntry from eventor-api to PersonCompetitor for database storage
     */
    private fun convertToPersonCompetitor(entry: PersonEntry, raceId: String): PersonCompetitor {
        return PersonCompetitor(
            id = entry.entryId,
            raceId = raceId,
            eventClassId = entry.classId,
            personId = entry.personId,
            name = entry.name,
            organisation = entry.organisation,
            birthYear = entry.birthYear,
            nationality = entry.nationality,
            gender = entry.gender,
            punchingUnit = entry.punchingUnits.firstOrNull(),
            bib = entry.bib,
            status = convertEntryStatusToCompetitorStatus(entry.status),
            startTime = entry.startTime,
            finishTime = entry.finishTime
        )
    }

    /**
     * Maps EntryStatus to CompetitorStatus
     */
    private fun convertEntryStatusToCompetitorStatus(entryStatus: EntryStatus): CompetitorStatus {
        return when (entryStatus) {
            EntryStatus.NotActivated -> CompetitorStatus.NotActivated
            EntryStatus.Activated -> CompetitorStatus.Activated
            EntryStatus.Started -> CompetitorStatus.Started
            EntryStatus.Finished -> CompetitorStatus.Finished
            EntryStatus.OK -> CompetitorStatus.Finished
            EntryStatus.MissingPunch -> CompetitorStatus.MissingPunch
            EntryStatus.Disqualified -> CompetitorStatus.Disqualified
            EntryStatus.DidNotFinish -> CompetitorStatus.DidNotFinish
            EntryStatus.Overtime -> CompetitorStatus.Overtime
            EntryStatus.NotCompeting -> CompetitorStatus.NotCompeting
            EntryStatus.SportWithdraw -> CompetitorStatus.SportWithdraw
            EntryStatus.NotStarted -> CompetitorStatus.NotStarted
            EntryStatus.DidNotStart -> CompetitorStatus.DidNotStart
            EntryStatus.Cancelled -> CompetitorStatus.Cancelled
            EntryStatus.Deregistered -> CompetitorStatus.Deregistered
        }
    }
}
