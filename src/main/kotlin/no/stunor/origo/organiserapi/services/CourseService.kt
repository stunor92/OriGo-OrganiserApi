package no.stunor.origo.organiserapi.services

import no.stunor.origo.organiserapi.exception.EventNotFoundException
import no.stunor.origo.organiserapi.data.CoursesRepository
import no.stunor.origo.organiserapi.data.EventRepository
import no.stunor.origo.organiserapi.model.courses.Control
import no.stunor.origo.organiserapi.model.courses.ControlType
import no.stunor.origo.organiserapi.model.courses.Course
import no.stunor.origo.organiserapi.model.courses.CourseVariant
import no.stunor.origo.organiserapi.model.courses.Leg
import no.stunor.origo.organiserapi.model.courses.Map
import no.stunor.origo.organiserapi.model.courses.MapPosition
import no.stunor.origo.organiserapi.model.courses.Position
import no.stunor.origo.organiserapi.model.event.EventClass
import org.iof.ClassCourseAssignment
import org.iof.CourseData
import org.iof.RaceCourseData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CourseService {

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var coursesRepository: CoursesRepository

    fun saveCourse(eventorId: String, eventId: String, raceId: String, courseData: CourseData) {
        if (courseData.raceCourseData.isEmpty()) {
            return
        }
        val event = eventRepository.findByEventIdAndEventorId(eventId, eventorId) ?: throw EventNotFoundException()
        val map = Map(
            raceId = raceId,
            scale = courseData.raceCourseData.first().map.first()?.scale,
            mapPosition = getMapPosition(courseData.raceCourseData.first().map.firstOrNull()),
            controls = getControls(courseData.raceCourseData.first()),
            courses = getCourses(courseData.raceCourseData.first(), event.eventClasses)
        )
        event.id?.let { coursesRepository.save(it, map) }

    }

    private fun getCourses(raceData: RaceCourseData, eventClasses: List<EventClass>?): List<Course> {
        val courses = mutableListOf<Course>()
        for(course in raceData.course) {
            if(!course.courseFamily.isNullOrBlank() && courses.any { it.name == course.courseFamily }) {
               courses.first { it.name == course.courseFamily }.variants.add(
                   CourseVariant(
                       name = course.name,
                       length = course.length,
                       climb = course.climb,
                       controls = getLegs(course.courseControl),
                       classes = getClasses(course, raceData.classCourseAssignment, eventClasses)
                   )
               )
            } else {
                courses.add(getCourse(course, raceData.classCourseAssignment, eventClasses))
            }
        }
        return courses
    }

    private fun getCourse(
        course: org.iof.Course,
        classCourseAssignment: List<ClassCourseAssignment>,
        eventClasses: List<EventClass>?, ): Course {
        return Course(
            name = if(course.courseFamily.isNullOrBlank()) course.name else course.courseFamily,
            variants = mutableListOf(
                CourseVariant(
                    name =  if(!course.courseFamily.isNullOrBlank()) course.name else null,
                    length = course.length,
                    climb = course.climb,
                    controls = getLegs(course.courseControl),
                    classes = getClasses(course, classCourseAssignment, eventClasses)
                )
            )
        )

    }

    private fun getClasses(
        course: org.iof.Course,
        classCourseAssignment: List<ClassCourseAssignment>,
        eventClasses: List<EventClass>?
    ): List<String> {
        val classNames = classCourseAssignment.filter { it.courseName == course.name }.map { it.className.uppercase() }
        return eventClasses?.filter { classNames.contains(it.name.uppercase()) }?.map { it.eventClassId } ?: listOf()
    }

    private fun getLegs(courseControls: List<org.iof.CourseControl>): List<Leg> {
        val legs = mutableListOf<Leg>()
        for (leg in courseControls) {
            legs.add(
                Leg(
                    controlCodes = leg.control,
                    mapText = leg.mapText,
                    lengt = leg.legLength
                )
            )
        }
        return legs
    }

    private fun getControls(raceData: RaceCourseData): List<Control> {
      val controls = mutableListOf<Control>()
        for(control in raceData.control) {
            controls.add(getControl(control, raceData.course))
        }
        return controls
    }

    private fun getControl(control: org.iof.Control, courses: List<org.iof.Course>): Control {
        return Control(
            type = getControlType(control, courses),
            controlCode = control.id.value,
            position = 1,//getGeoPoint(control.position),
            mapPosition = getPosition(control.mapPosition)
        )
    }

    private fun getControlType(control: org.iof.Control, courses: List<org.iof.Course>): ControlType {
        if(control.type != null) {
            return convertControlType(control.type)
        }
        for(course in courses) {
            for(courseControl in course.courseControl) {
                if(courseControl.control.contains(control.id.value)) {
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

    /*private fun getGeoPoint(position: org.iof.GeoPosition?): GeoPoint? {
        if (position == null) {
            return null
        }
        return GeoPoint(position.lat, position.lng)
    }*/

    private fun getMapPosition(map: org.iof.Map?): MapPosition? {
        if (map == null) {
            return null
        }
        return MapPosition(
            topLeftPosition = getPosition(map.mapPositionTopLeft),
            bottomRightPosition = getPosition(map.mapPositionBottomRight)
        )

    }

    private fun getPosition(mapPosition: org.iof.MapPosition): Position {

        return Position(
            x = mapPosition.x,
            y = mapPosition.y,
            unit = mapPosition.unit
        )

    }
}