package no.stunor.origo.organiserapi.model.event

import java.io.Serializable


data class EventClass (
        var eventClassId: String = "",
        var name: String = "",
        var shortName: String = "",
) : Serializable