package com.kaltt.agenda

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.apis.CalendarAPI
import com.kaltt.agenda.apis.FirebaseAPI
import com.kaltt.agenda.apis.FirestoreAPI
import com.kaltt.agenda.classes.DataEventFather
import com.kaltt.agenda.classes.DataUser
import com.kaltt.agenda.classes.Difference
import com.kaltt.agenda.classes.enums.FromType
import com.kaltt.agenda.classes.events.EventFather
import java.util.*
import kotlin.collections.ArrayList

class MainRepository {
    companion object {
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        private val firebaseAPI = FirebaseAPI.getInstance()
        private val v: V = V.getInstance()

        val email: String
            get() = firebaseAPI.email

        fun checkUser(activity : Activity) {
            if(!firebaseAPI.isSigned) {
                activity.startActivity(Intent(activity, LoginActivity::class.java))
                activity.finish()
            }
        }

        fun signOut(activity: Activity) {
            // TODO borrar eventos
            firebaseAPI.signOut()
            // TODO borrar user default
            checkUser(activity)
        }
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        private val firestoreAPI = FirestoreAPI.getInstance()

        fun setUser(user: FirebaseUser) = firestoreAPI.setUser(DataUser.from(user))

        suspend fun getUsers() {
            //TODO
            firestoreAPI.getUsers()
        }
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        suspend fun fetchGoogleEvent() {
            val response = CalendarAPI.fetchEvents()
            val google = (
                    if(response.isSuccessful)
                        ArrayList(response.body()!!.items.map { EventFather.from(it) })
                    else
                        ArrayList()
                    )
            google.sortBy { it.start }
            val filtered = ArrayList<EventFather>()
            google.forEach {
                val similars = filtered.filter { e -> it.name == e.name }
                var absent = true
                for (similar in similars) {
                    if(similar.start.isEqual(it.end)) {
                        val diff = Difference.between(it.start, similar.end)
                        similar.start = it.start
                        similar.end = diff.applyOn(similar.start)
                        absent = false
                        break
                    } else if(similar.end.isEqual(it.start)) {
                        similar.end = it.end
                        absent = false
                        break
                    }
                }
                if(absent) {
                    filtered.add(it)
                }
            }
            val c = listOf(4.0,44.0,137.0,216.0)
            var n = 0
            filtered.forEach {
                it.color = c[n]
                if(n == 3) n = 0 else n += 1
            }
            v.googleEvents = filtered
        }

        suspend fun fetchEvents() {
            // TODO
            //if( room.google.lastCheck > semanaPasada) {
                //fetchGoogleEvent()
            //}
            // TODO check ultima actualziacion
            val allEvents = ArrayList(firestoreAPI.getEvents(firebaseAPI.email).map { DataEventFather.from(it)})
            v.ownedEvents = ArrayList(allEvents
                .filter { it.owner == firebaseAPI.email }
                .map { EventFather.from(FromType.OWNED, it)}
            )
            v.sharedEvents = ArrayList(allEvents
                    .filter { it.sharedWith.contains(firebaseAPI.email) }
                    .map { EventFather.from(FromType.SHARED, it)}
            )
        }
        suspend fun saveEvent(e: EventFather) {
            if(e.id.isBlank()) {
                e.id = UUID.randomUUID().toString()
            }
            firestoreAPI.save("events", e.id, e.toMap())
        }
    }
}