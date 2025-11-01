package no.stunor.origo.organiserapi.data

import no.stunor.origo.organiserapi.model.event.Race
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RaceRepository : CrudRepository<Race, UUID>
