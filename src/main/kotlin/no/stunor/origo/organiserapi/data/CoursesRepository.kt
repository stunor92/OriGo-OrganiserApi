package no.stunor.origo.organiserapi.data

import no.stunor.origo.organiserapi.model.courses.Map
import org.springframework.stereotype.Repository


@Repository
class CoursesRepository {
    //private val firestore = FirestoreClient.getFirestore()

    fun save(eventDocument: String, map: Map) {
        //firestore.collection("events").document(eventDocument).collection("maps").add(map)
    }
}