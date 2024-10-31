package no.stunor.origo.organiserapi.controller

import no.stunor.origo.organiserapi.services.CourseService
import org.iof.CourseData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
internal class CoursesController {

    @Autowired
    private lateinit var courseService: CourseService

    @PostMapping("/courses/{eventorId}/{eventId}/{raceId}")
    fun postResult(
        @PathVariable(value = "eventorId") eventorId: String,
        @PathVariable(value = "eventId") eventId: String,
        @PathVariable(value = "raceId") raceId: String,
        @RequestBody(required = true) courseData: CourseData
    ) {
        courseService.saveCourse(eventorId, eventId, raceId, courseData)
    }

}