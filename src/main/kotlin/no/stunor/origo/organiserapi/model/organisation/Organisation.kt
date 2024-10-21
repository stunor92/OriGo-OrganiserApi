package no.stunor.origo.organiserapi.model.organisation

data class Organisation (
        var organisationId: String? = null,
        var name: String = "",
        var type: OrganisationType = OrganisationType.Club,
        var country: String? = null
)
