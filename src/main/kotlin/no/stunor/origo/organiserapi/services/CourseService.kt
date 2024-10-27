package no.stunor.origo.organiserapi.services

import com.google.cloud.firestore.GeoPoint
import no.stunor.origo.organiserapi.model.courses.Control
import no.stunor.origo.organiserapi.model.courses.ControlType
import no.stunor.origo.organiserapi.model.courses.Map
import no.stunor.origo.organiserapi.model.courses.MapPosition
import no.stunor.origo.organiserapi.model.courses.Position
import org.iof.CourseData
import org.springframework.stereotype.Service

@Service
class CourseService {

    fun saveCourse(record: String, eventId: String, raceId: String, courseData: CourseData) {
        if (courseData.raceCourseData.isEmpty()) {
            return
        }
        var map: Map = Map(
            raceId = raceId,
            scale = courseData.raceCourseData.first().map.first()?.scale,
            mapPosition = getMapPosition(courseData.raceCourseData.first().map.firstOrNull()),
            controls = getControls(courseData.raceCourseData.first()),
            courses = listOf()
        )

    }

    private fun getControls(raceData: org.iof.RaceCourseData): List<Control> {
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
            position = getGeoPoint(control.position),
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

    private fun getGeoPoint(position: org.iof.GeoPosition?): GeoPoint? {
        if (position == null) {
            return null
        }
        return GeoPoint(position.lat, position.lng)
    }

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