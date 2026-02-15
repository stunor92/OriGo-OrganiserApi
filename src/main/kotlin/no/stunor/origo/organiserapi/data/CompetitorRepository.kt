package no.stunor.origo.organiserapi.data

import no.stunor.origo.organiserapi.model.competitor.CompetitorStatus
import no.stunor.origo.organiserapi.model.competitor.PersonCompetitor
import no.stunor.origo.organiserapi.model.competitor.PunchingUnit
import no.stunor.origo.organiserapi.model.competitor.PunchingUnitType
import no.stunor.origo.organiserapi.model.organisation.Organisation
import no.stunor.origo.organiserapi.model.organisation.OrganisationType
import no.stunor.origo.organiserapi.model.person.Gender
import no.stunor.origo.organiserapi.model.person.PersonName
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
open class CompetitorRepository(private val jdbcTemplate: JdbcTemplate) {

    private val personCompetitorRowMapper = RowMapper { rs: ResultSet, _: Int ->
        PersonCompetitor(
            id = rs.getString("id"),
            raceId = rs.getString("race_id") ?: "",
            eventClassId = rs.getString("event_class_id") ?: "",
            personId = rs.getString("person_id"),
            name = PersonName(
                given = rs.getString("given_name") ?: "",
                family = rs.getString("family_name") ?: ""
            ),
            organisation = rs.getString("organisation_id")?.let {
                Organisation(
                    organisationId = it,
                    name = rs.getString("organisation_name") ?: "",
                    type = rs.getString("organisation_type")?.let { type ->
                        try { OrganisationType.valueOf(type) } catch (e: Exception) { OrganisationType.Club }
                    } ?: OrganisationType.Club,
                    country = rs.getString("organisation_country") ?: ""
                )
            },
            birthYear = rs.getInt("birth_year").takeIf { rs.wasNull().not() },
            nationality = rs.getString("nationality"),
            gender = rs.getString("gender")?.let { 
                try { Gender.valueOf(it) } catch (e: Exception) { Gender.Other }
            } ?: Gender.Other,
            punchingUnit = rs.getString("punching_unit_id")?.let {
                PunchingUnit(
                    id = it,
                    type = rs.getString("punching_unit_type")?.let { type ->
                        try { PunchingUnitType.valueOf(type) } catch (e: Exception) { PunchingUnitType.Emit }
                    } ?: PunchingUnitType.Emit
                )
            },
            bib = rs.getString("bib"),
            status = rs.getString("status")?.let { 
                try { CompetitorStatus.valueOf(it) } catch (e: Exception) { CompetitorStatus.NotActivated }
            } ?: CompetitorStatus.NotActivated,
            startTime = rs.getTimestamp("start_time"),
            finishTime = rs.getTimestamp("finish_time")
        )
    }

    open fun save(competitor: PersonCompetitor): PersonCompetitor {
        val id = competitor.id ?: UUID.randomUUID().toString()
        
        val exists = try {
            jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM competitor WHERE id = ?",
                Int::class.java,
                id
            )!! > 0
        } catch (_: Exception) {
            false
        }

        if (exists) {
            jdbcTemplate.update(
                """
                UPDATE competitor SET 
                    race_id = ?,
                    event_class_id = ?,
                    person_id = ?,
                    given_name = ?,
                    family_name = ?,
                    organisation_id = ?,
                    organisation_name = ?,
                    organisation_type = ?,
                    organisation_country = ?,
                    birth_year = ?,
                    nationality = ?,
                    gender = ?,
                    punching_unit_id = ?,
                    punching_unit_type = ?,
                    bib = ?,
                    status = ?,
                    start_time = ?,
                    finish_time = ?
                WHERE id = ?
                """.trimIndent(),
                competitor.raceId,
                competitor.eventClassId,
                competitor.personId,
                (competitor.name as PersonName).given,
                (competitor.name as PersonName).family,
                competitor.organisation?.organisationId,
                competitor.organisation?.name,
                competitor.organisation?.type?.name,
                competitor.organisation?.country,
                competitor.birthYear,
                competitor.nationality,
                competitor.gender.name,
                competitor.punchingUnit?.id,
                competitor.punchingUnit?.type?.name,
                competitor.bib,
                competitor.status.name,
                competitor.startTime,
                competitor.finishTime,
                id
            )
        } else {
            jdbcTemplate.update(
                """
                INSERT INTO competitor (
                    id, race_id, event_class_id, person_id, 
                    given_name, family_name,
                    organisation_id, organisation_name, organisation_type, organisation_country,
                    birth_year, nationality, gender,
                    punching_unit_id, punching_unit_type,
                    bib, status, start_time, finish_time
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                id,
                competitor.raceId,
                competitor.eventClassId,
                competitor.personId,
                (competitor.name as PersonName).given,
                (competitor.name as PersonName).family,
                competitor.organisation?.organisationId,
                competitor.organisation?.name,
                competitor.organisation?.type?.name,
                competitor.organisation?.country,
                competitor.birthYear,
                competitor.nationality,
                competitor.gender.name,
                competitor.punchingUnit?.id,
                competitor.punchingUnit?.type?.name,
                competitor.bib,
                competitor.status.name,
                competitor.startTime,
                competitor.finishTime
            )
        }

        return competitor.copy(id = id)
    }

    open fun findById(id: String): PersonCompetitor? {
        return try {
            jdbcTemplate.queryForObject(
                "SELECT * FROM competitor WHERE id = ?",
                personCompetitorRowMapper,
                id
            )
        } catch (_: Exception) {
            null
        }
    }

    open fun findByRaceId(raceId: String): List<PersonCompetitor> {
        return jdbcTemplate.query(
            "SELECT * FROM competitor WHERE race_id = ?",
            personCompetitorRowMapper,
            raceId
        )
    }

    open fun deleteByRaceId(raceId: String) {
        jdbcTemplate.update("DELETE FROM competitor WHERE race_id = ?", raceId)
    }
}
