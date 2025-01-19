package no.stunor.origo.organiserapi.controller

import no.stunor.origo.organiserapi.services.CourseService
import org.iof.CourseData
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
internal class CoursesController {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var courseService: CourseService

    @PostMapping("/courses/{eventorId}/{eventId}/{raceId}")
    fun postResult(
        @PathVariable(value = "eventorId") eventorId: String,
        @PathVariable(value = "eventId") eventId: String,
        @PathVariable(value = "raceId") raceId: String,
        @RequestBody(required = true) courseData: CourseData
    ) {
        log.info("Saving course for eventorId: $eventorId, eventId: $eventId, raceId: $raceId")
        log.info("Course data: $courseData")

        courseService.saveCourse(eventorId, eventId, raceId, courseData)
    }

}