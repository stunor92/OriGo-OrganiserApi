package no.stunor.origo.organiserapi.model.courses

import java.util.*

data class Course (
    var id: UUID? = null,
    var name: String,
    var mapId: UUID? = null,
    var variants: MutableSet<CourseVariant> = mutableSetOf()
) {
    override fun toString(): String {
        return "Course(name='$name')"
    }
}
