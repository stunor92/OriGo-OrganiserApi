package no.stunor.origo.organiserapi.data

import no.stunor.origo.organiserapi.model.courses.RaceMap
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface MapRepository : CrudRepository<RaceMap, UUID>