package no.stunor.origo.organiserapi.controller

import jakarta.xml.bind.JAXBContext
import no.stunor.origo.organiserapi.services.CourseService
import org.iof.CourseData
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.StringReader
import java.util.UUID

@RestController
internal class CoursesController {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    private lateinit var courseService: CourseService

    // Initialize JAXB context once for performance
    private val jaxbContext = JAXBContext.newInstance(CourseData::class.java)

    @PostMapping("/courses/{raceId}", consumes = ["application/xml", "text/xml"])
    fun postResult(
        @PathVariable(value = "raceId") raceId: UUID,
        @RequestHeader(required = true, value = "Map-Name") name: String,
        @RequestBody(required = true) xmlBody: String
    ) {
        // Use JAXB to unmarshal the XML
        val courseData = try {
            val unmarshaller = jaxbContext.createUnmarshaller()
            unmarshaller.unmarshal(StringReader(xmlBody)) as CourseData
        } catch (e: Exception) {
            log.error("Failed to parse XML with JAXB", e)
            throw IllegalArgumentException("Failed to parse CourseData XML: ${e.message}", e)
        }

        courseService.saveCourse(raceId, name, courseData)
    }

}