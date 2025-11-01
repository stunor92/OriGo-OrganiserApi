package no.stunor.origo.organiserapi.model.event

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.sql.Timestamp
import java.util.*


@Entity
data class Event(
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    var id: UUID = UUID.randomUUID(),
    @JsonIgnore var eventorId: String = "",
    var eventorRef: String = "",
    var name: String = "",
    var startDate: Timestamp? = null,
    var finishDate: Timestamp? = null,
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "event") var classes: MutableList<EventClass> = mutableListOf()
) {
    override fun toString(): String {
        return "Event(name='$name')"
    }
}