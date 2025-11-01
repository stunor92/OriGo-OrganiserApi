package no.stunor.origo.organiserapi.model.courses

import jakarta.persistence.*
import no.stunor.origo.organiserapi.model.event.EventClass
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
data class Course (
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    var id: UUID? = null,
    var name: String,
    @ManyToOne
    @JoinColumn(name = "map_id")
    var map: RaceMap? = null,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "course")
    var variants: MutableList<CourseVariant> = mutableListOf(),
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "class_course",
        joinColumns = [JoinColumn(name = "course_id")],
        inverseJoinColumns = [JoinColumn(name = "class_id")]
    )
    var classes: MutableList<EventClass> = mutableListOf(),
) {
    override fun toString(): String {
        return "Course(name='$name')"
    }
}
