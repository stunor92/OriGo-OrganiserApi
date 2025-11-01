package no.stunor.origo.organiserapi.model.event

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.sql.Timestamp
import java.util.UUID


@Entity
data class Race(
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    var id: UUID = UUID.randomUUID(),
    var eventorRef: String = "",
    var name: String = "",
    var date: Timestamp? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore var event: Event = Event()
)