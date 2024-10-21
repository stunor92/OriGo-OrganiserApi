package no.stunor.origo.organiserapi.model.competitor

import java.io.Serializable

data class Result(
        var time: Int? = null,
        var timeBehind: Int? =  null,
        val position: Int? = null,
        val status: ResultStatus = ResultStatus.OK
)