package no.stunor.origo.organiserapi.model.courses

data class CourseVariant (
    var name: String?,
    var length: Int?,
    var climb: Int?,
    var controls: List<Leg> = listOf(),
    var classes: List<String> = listOf()
)
