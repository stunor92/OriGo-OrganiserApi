package no.stunor.origo.security.controller

import com.google.firebase.auth.FirebaseAuth

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
internal class Controller {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/token")
    fun createToken(
        @RequestHeader(value = "uid") uid: String
    ): ResponseEntity<String> {
        log.info("Get token for user {}.", uid)
        val token = FirebaseAuth.getInstance().createCustomToken(uid)
        return ResponseEntity(token, HttpStatus.OK)
    }
}
