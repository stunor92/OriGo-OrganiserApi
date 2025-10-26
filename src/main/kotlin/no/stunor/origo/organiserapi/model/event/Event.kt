package no.stunor.origo.organiserapi.model.event

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import java.util.*

data class Event (
        @JsonIgnore
        var id: String? = null,
        var eventorId: String? = null,
        var eventId: String = "",
        var eventClasses: List<EventClass> = ArrayList(),
) : Serializable
