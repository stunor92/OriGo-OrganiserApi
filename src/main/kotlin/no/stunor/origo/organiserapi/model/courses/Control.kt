package no.stunor.origo.organiserapi.model.courses

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
data class Control (
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    var id: UUID? = null,
    @Enumerated(EnumType.STRING) var type: ControlType?,
    var controlCode: String,
    @Embedded var mapPosition: MapPosition,
    @Embedded var geoPosition: GeoPosition?,
    @ManyToOne
    @JoinColumn(name = "map_id")
    var map: RaceMap? = null
) {
    override fun toString(): String = "Control(controlCode='$controlCode', type=$type)"
}