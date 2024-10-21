package no.stunor.origo.organiserapi.model.person

import com.fasterxml.jackson.annotation.JsonIgnore
import com.google.cloud.firestore.annotation.DocumentId
import java.io.Serializable

data class Person(
        @DocumentId
        var id: String? = null,
        var eventorId: String = "",
        var personId: String = "",
        var name: PersonName = PersonName(),
        var birthYear: Int = 0,
        var nationality: String = "",
        var gender: Gender = Gender.Other,
        var mobilePhone: String? = null,
        var email: String? = null,
        var memberships: Map<String, MembershipType> = HashMap(),
)
