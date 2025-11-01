package no.stunor.origo.organiserapi.model.courses

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*


@Entity
data class Leg (
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    var id: UUID? = null,
    var sequenceNumber: Int = 0,
    @ManyToOne
    @JoinColumn(name = "course_variant_id")
    var courseVariant: CourseVariant? = null,
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "leg_control",
        joinColumns = [JoinColumn(name = "leg_id")],
        inverseJoinColumns = [JoinColumn(name = "control_id")]
    )
    var controls: MutableList<Control> = mutableListOf(),
    var length: Double? = null
) {
    override fun toString(): String {
        return "Leg(sequenceNumber=$sequenceNumber)"
    }
}