package no.stunor.origo.organiserapi.services

import no.stunor.origo.organiserapi.data.EventRepository
import no.stunor.origo.organiserapi.data.MapRepository
import no.stunor.origo.organiserapi.data.RaceRepository
import no.stunor.origo.organiserapi.exception.EventNotFoundException
import no.stunor.origo.organiserapi.model.courses.Control
import no.stunor.origo.organiserapi.model.courses.ControlType
import no.stunor.origo.organiserapi.model.courses.Course
import no.stunor.origo.organiserapi.model.courses.CourseVariant
import no.stunor.origo.organiserapi.model.courses.GeoPosition
import no.stunor.origo.organiserapi.model.courses.Leg
import no.stunor.origo.organiserapi.model.courses.MapArea
import no.stunor.origo.organiserapi.model.courses.MapPosition
import no.stunor.origo.organiserapi.model.courses.RaceMap
import no.stunor.origo.organiserapi.model.event.EventClass
import org.iof.ClassCourseAssignment
import org.iof.CourseData
import org.iof.RaceCourseData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CourseService {

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var raceRepository: RaceRepository

    @Autowired
    private lateinit var mapRepository: MapRepository

    fun saveCourse(raceId: UUID, courseData: CourseData) {
        if (courseData.raceCourseData.isEmpty()) return
        val race = raceRepository.findById(raceId).orElseThrow { EventNotFoundException() }
        val event = eventRepository.findById(race.event.id).orElseThrow { EventNotFoundException() }
        val controls = getControls(courseData.raceCourseData.first())
        val courses = getCourses(courseData.raceCourseData.first(), event.classes, controls)
        val map = RaceMap(
            raceId = race.id,
            scale = courseData.raceCourseData.first().map.first()?.scale,
            mapArea = getMapArea(courseData.raceCourseData.first().map.firstOrNull()),
            controls = controls,
            courses = courses
        )
        controls.forEach { it.map = map }
        courses.forEach { c -> c.map = map; c.variants.forEach { v -> v.course = c; v.controls.forEach { l -> l.courseVariant = v } } }
        mapRepository.save(map)
    }

    private fun getCourses(
        raceData: RaceCourseData,
        classes: List<EventClass>,
        controls: List<Control>
    ): MutableList<Course> {
        val courses = mutableListOf<Course>()
        for (course in raceData.course) {
            if (!course.courseFamily.isNullOrBlank() && courses.any { it.name == course.courseFamily }) {
                val existingCourse = courses.first { it.name == course.courseFamily }
                val legs = getLegs(course.courseControl, controls)
                val variant = CourseVariant(
                    name = course.name,
                    length = course.length,
                    climb = course.climb,
                    course = existingCourse,
                    controls = legs
                )
                legs.forEach { it.courseVariant = variant }
                existingCourse.variants.add(variant)
                // Add all classes whose IDs are returned by getClassIds, avoiding duplicates
                val classIds = getClassIds(course, raceData.classCourseAssignment, classes)
                val matchingClasses = classes.filter { it.id in classIds }
                matchingClasses.forEach { ec -> if (existingCourse.classes.none { it.id == ec.id }) existingCourse.classes.add(ec) }
            } else {
                courses.add(getCourse(course, raceData.classCourseAssignment, classes, controls))
            }
        }
        return courses
    }

    private fun getCourse(
        course: org.iof.Course,
        classCourseAssignment: List<ClassCourseAssignment>,
        classes: List<EventClass>,
        controls: List<Control>
    ): Course {
        val classIds = getClassIds(course, classCourseAssignment, classes)
        val newCourse = Course(
            name = if (course.courseFamily.isNullOrBlank()) course.name else course.courseFamily,
        )
        val legs = getLegs(course.courseControl, controls)
        val variant = CourseVariant(
            name = if (!course.courseFamily.isNullOrBlank()) course.name else null,
            length = course.length,
            climb = course.climb,
            course = newCourse,
            controls = legs
        )
        legs.forEach { it.courseVariant = variant }
        newCourse.variants.add(variant)
        newCourse.classes = classes.filter { it.id in classIds }.toMutableList()
        return newCourse
    }

    private fun getClassIds(
        course: org.iof.Course,
        classCourseAssignment: List<ClassCourseAssignment>,
        eventClasses: List<EventClass>
    ): List<UUID> {
        val classNames = classCourseAssignment.filter { it.courseName == course.name }.map { it.className.uppercase() }
        return eventClasses.filter { classNames.contains(it.name.uppercase()) }.map { it.id }
    }

    private fun getLegs(
        courseControls: List<org.iof.CourseControl>,
        controls: List<Control>
    ): MutableList<Leg> {
        val legs = mutableListOf<Leg>()
        courseControls.forEachIndexed { index, legDef ->
            val matchingControls: MutableList<Control> = controls.filter { ctrl -> legDef.control.contains(ctrl.controlCode) }.toMutableList()
            legs.add(
                Leg(
                    sequenceNumber = (legDef.mapText?.toIntOrNull() ?: (index + 1)),
                    controls = matchingControls,
                    length = legDef.legLength
                )
            )
        }
        return legs
    }

    private fun getControls(raceData: RaceCourseData): MutableList<Control> {
        val controls = mutableListOf<Control>()
        for (control in raceData.control) {
            controls.add(getControl(control, raceData.course))
        }
        return controls
    }

    private fun getControl(control: org.iof.Control, courses: List<org.iof.Course>): Control {
        return Control(
            type = getControlType(control, courses),
            controlCode = control.id.value,
            geoPosition = getGeoPosition(control.position),
            mapPosition = getPosition(control.mapPosition)
        )
    }

    private fun getControlType(control: org.iof.Control, courses: List<org.iof.Course>): ControlType {
        if (control.type != null) {
            return convertControlType(control.type)
        }
        for (course in courses) {
            for (courseControl in course.courseControl) {
                if (courseControl.control.contains(control.id.value)) {
                    return convertControlType(courseControl.type)
                }
            }
        }
        return ControlType.Control
    }

    private fun convertControlType(type: org.iof.ControlType): ControlType {
        return when (type) {
            org.iof.ControlType.START -> ControlType.Start
            org.iof.ControlType.FINISH -> ControlType.Finish
            org.iof.ControlType.CROSSING_POINT -> ControlType.CrossingPoint
            org.iof.ControlType.END_OF_MARKED_ROUTE -> ControlType.EndOfMarkedRoute
            else -> ControlType.Control
        }
    }

    private fun getGeoPosition(position: org.iof.GeoPosition?): GeoPosition? {
        if (position == null) {
            return null
        }
        return GeoPosition(position.lat, position.lng)
    }

    private fun getMapArea(map: org.iof.Map?): MapArea? {
        if (map == null) {
            return null
        }
        return MapArea(
            topLeftPosition = getPosition(map.mapPositionTopLeft),
            bottomRightPosition = getPosition(map.mapPositionBottomRight)
        )

    }

    private fun getPosition(mapPosition: org.iof.MapPosition): MapPosition {

        return MapPosition(
            x = mapPosition.x,
            y = mapPosition.y,
            //unit = mapPosition.unit
        )

    }
}