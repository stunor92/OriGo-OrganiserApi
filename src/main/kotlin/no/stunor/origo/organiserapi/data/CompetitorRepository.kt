package no.stunor.origo.organiserapi.data

import no.stunor.origo.organiserapi.model.competitor.CompetitorStatus
import no.stunor.origo.organiserapi.model.competitor.PersonCompetitor
import no.stunor.origo.organiserapi.model.competitor.ResultStatus
import no.stunor.origo.organiserapi.model.person.Gender
import no.stunor.origo.organiserapi.model.person.PersonName
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
open class CompetitorRepository(private val jdbcTemplate: JdbcTemplate) {

    private val personEntryRowMapper = RowMapper { rs: ResultSet, _: Int ->
        PersonCompetitor(
            id = rs.getString("id"),
            raceId = UUID.fromString(rs.getString("race_id")).toString(),
            eventClassId = UUID.fromString(rs.getString("class_id")).toString(),
            personId = rs.getString("person_eventor_ref"),
            name = PersonName(
                given = rs.getString("given_name") ?: "",
                family = rs.getString("family_name") ?: ""
            ),
            organisation = null, // Organizations are in separate table entry_organisation
            birthYear = rs.getLong("birth_year").takeIf { !rs.wasNull() }?.toInt(),
            nationality = rs.getString("nationality"),
            gender = rs.getString("gender")?.let { 
                try { Gender.valueOf(it) } catch (e: Exception) { Gender.Other }
            } ?: Gender.Other,
            punchingUnit = null, // Punching units are in separate table punching_unit_entry
            bib = rs.getString("bib"),
            status = rs.getString("status")?.let { 
                try { CompetitorStatus.valueOf(it) } catch (e: Exception) { CompetitorStatus.NotActivated }
            } ?: CompetitorStatus.NotActivated,
            startTime = rs.getTimestamp("start_time"),
            finishTime = rs.getTimestamp("finish_time"),
            result = rs.getString("result_status")?.let { statusStr ->
                rs.getLong("time").takeIf { !rs.wasNull() }?.let { time ->
                    no.stunor.origo.organiserapi.model.competitor.Result(
                        time = time.toInt(),
                        timeBehind = rs.getLong("time_behind").takeIf { !rs.wasNull() }?.toInt() ?: 0,
                        position = rs.getLong("position").takeIf { !rs.wasNull() }?.toInt() ?: 0,
                        status = try { ResultStatus.valueOf(statusStr) } catch (e: Exception) { ResultStatus.Inactive }
                    )
                }
            }
        )
    }

    open fun save(competitor: PersonCompetitor): PersonCompetitor {
        val id = competitor.id?.let { UUID.fromString(it) } ?: UUID.randomUUID()
        
        val exists = try {
            jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM person_entry WHERE id = ?::uuid",
                Int::class.java,
                id.toString()
            )!! > 0
        } catch (_: Exception) {
            false
        }

        val raceId = UUID.fromString(competitor.raceId)
        val classId = UUID.fromString(competitor.eventClassId)

        if (exists) {
            jdbcTemplate.update(
                """
                UPDATE person_entry SET 
                    race_id = ?::uuid,
                    class_id = ?::uuid,
                    person_eventor_ref = ?,
                    eventor_ref = ?,
                    given_name = ?,
                    family_name = ?,
                    birth_year = ?,
                    nationality = ?,
                    gender = ?::gender,
                    bib = ?,
                    status = ?::competitor_status,
                    start_time = ?,
                    finish_time = ?,
                    time = ?,
                    time_behind = ?,
                    position = ?,
                    result_status = ?::result_status
                WHERE id = ?::uuid
                """.trimIndent(),
                raceId,
                classId,
                competitor.personId,
                competitor.id, // eventor_ref same as id for now
                (competitor.name as PersonName).given,
                (competitor.name as PersonName).family,
                competitor.birthYear?.toLong(),
                competitor.nationality,
                competitor.gender.name,
                competitor.bib,
                competitor.status.name,
                competitor.startTime,
                competitor.finishTime,
                competitor.result?.time?.toLong(),
                competitor.result?.timeBehind?.toLong(),
                competitor.result?.position?.toLong(),
                competitor.result?.status?.name,
                id
            )
        } else {
            jdbcTemplate.update(
                """
                INSERT INTO person_entry (
                    id, race_id, class_id, person_eventor_ref, eventor_ref,
                    given_name, family_name,
                    birth_year, nationality, gender,
                    bib, status, start_time, finish_time,
                    time, time_behind, position, result_status
                ) VALUES (?::uuid, ?::uuid, ?::uuid, ?, ?, ?, ?, ?, ?, ?::gender, ?, ?::competitor_status, ?, ?, ?, ?, ?, ?::result_status)
                """.trimIndent(),
                id,
                raceId,
                classId,
                competitor.personId,
                competitor.id, // eventor_ref same as id for now
                (competitor.name as PersonName).given,
                (competitor.name as PersonName).family,
                competitor.birthYear?.toLong(),
                competitor.nationality,
                competitor.gender.name,
                competitor.bib,
                competitor.status.name,
                competitor.startTime,
                competitor.finishTime,
                competitor.result?.time?.toLong(),
                competitor.result?.timeBehind?.toLong(),
                competitor.result?.position?.toLong(),
                competitor.result?.status?.name
            )
        }

        // Handle punching units in separate table if present
        competitor.punchingUnit?.let { savePunchingUnit(id, it) }

        return competitor.copy(id = id.toString())
    }

    private fun savePunchingUnit(entryId: UUID, punchingUnit: no.stunor.origo.organiserapi.model.competitor.PunchingUnit) {
        // Delete existing punching units for this person entry (leg IS NULL for person entries, leg has value for team members)
        jdbcTemplate.update(
            "DELETE FROM punching_unit_entry WHERE entry_id = ?::uuid AND leg IS NULL",
            entryId
        )
        
        // Insert new punching unit
        jdbcTemplate.update(
            """
            INSERT INTO punching_unit_entry (entry_id, leg, id, type)
            VALUES (?::uuid, NULL, ?, ?::punching_unit_type)
            ON CONFLICT DO NOTHING
            """.trimIndent(),
            entryId,
            punchingUnit.id,
            punchingUnit.type.name
        )
    }

    open fun findById(id: String): PersonCompetitor? {
        return try {
            jdbcTemplate.queryForObject(
                "SELECT * FROM person_entry WHERE id = ?::uuid",
                personEntryRowMapper,
                id
            )
        } catch (_: Exception) {
            null
        }
    }

    open fun findByRaceId(raceId: String): List<PersonCompetitor> {
        return jdbcTemplate.query(
            "SELECT * FROM person_entry WHERE race_id = ?::uuid",
            personEntryRowMapper,
            raceId
        )
    }

    open fun deleteByRaceId(raceId: String) {
        jdbcTemplate.update("DELETE FROM person_entry WHERE race_id = ?::uuid", raceId)
    }
}
