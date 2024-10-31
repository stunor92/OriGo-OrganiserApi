package no.stunor.origo.organiserapi.model.courses

data class Course(
        var name: String,
        var variants: MutableList<CourseVariant> = mutableListOf()
)