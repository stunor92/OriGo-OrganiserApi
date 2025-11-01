package no.stunor.origo.organiserapi.model.courses

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
data class CourseVariant (
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    var id: UUID? = null,
    var name: String?,
    var length: Double?,
    var climb: Double?,
    @ManyToOne
    @JoinColumn(name = "course_id")
    var course: Course? = null,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "courseVariant")
    var controls: MutableList<Leg> = mutableListOf(),
    var printedMaps: Int? = null
) {
    override fun toString(): String {
        return "CourseVariant(name=$name)"
    }
}