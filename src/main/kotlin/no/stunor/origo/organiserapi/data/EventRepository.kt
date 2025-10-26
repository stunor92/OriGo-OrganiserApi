package no.stunor.origo.organiserapi.data

import no.stunor.origo.organiserapi.model.event.Event
import org.springframework.stereotype.Repository


@Repository
class EventRepository {
    //private val firestore = FirestoreClient.getFirestore()

    fun findByEventIdAndEventorId(eventId: String, eventorId: String): Event? {
       /* val future: ApiFuture<QuerySnapshot> = firestore.collection("events")
            .whereEqualTo("eventId", eventId)
            .whereEqualTo("eventorId", eventorId)
            .get()

        return if(future.get().isEmpty){
            null
        } else {
            future.get().documents.first().toObject(Event::class.java)
        }*/
        return null
    }
}