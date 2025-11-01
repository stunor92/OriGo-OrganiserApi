package no.stunor.origo.organiserapi.controller

import no.stunor.origo.organiserapi.services.CourseService
import org.iof.CourseData
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
internal class CoursesController {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var courseService: CourseService

    @PostMapping("/courses/{raceId}")
    fun postResult(
        @PathVariable(value = "raceId") raceId: UUID,
        @RequestBody(required = true) courseData: CourseData
    ) {
        log.info("Saving course for raceId: $raceId")
        log.info("Course data: $courseData")

        courseService.saveCourse(raceId, courseData)
    }

}