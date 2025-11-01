package no.stunor.origo.organiserapi.model.courses

import jakarta.persistence.Embedded
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column

@jakarta.persistence.Embeddable
data class MapArea (
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "x", column = Column(name = "top_left_x")),
        AttributeOverride(name = "y", column = Column(name = "top_left_y"))
    )
    var topLeftPosition: MapPosition = MapPosition(),

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "x", column = Column(name = "bottom_right_x")),
        AttributeOverride(name = "y", column = Column(name = "bottom_right_y"))
    )
    var bottomRightPosition: MapPosition = MapPosition()
)
