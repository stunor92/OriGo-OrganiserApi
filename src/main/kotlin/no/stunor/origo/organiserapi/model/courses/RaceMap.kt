package no.stunor.origo.organiserapi.model.courses

import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@Table(name = "map")
data class RaceMap (
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    var id: UUID? = null,
    var raceId: UUID = UUID.randomUUID(),
    var scale: Double?,
    @Embedded var mapArea: MapArea?,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "map") var controls: MutableList<Control> = ArrayList(),
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "map") var courses: MutableList<Course> = mutableListOf(),
) {
    override fun toString(): String {
        return "RaceMap(raceId=$raceId, id=$id)"
    }
}