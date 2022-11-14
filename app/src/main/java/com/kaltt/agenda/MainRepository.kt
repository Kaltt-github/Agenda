package com.kaltt.agenda

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.apis.CalendarAPI
import com.kaltt.agenda.classes.ClassAdapter
import com.kaltt.agenda.apis.FirebaseAPI
import com.kaltt.agenda.apis.FirestoreAPI
import com.kaltt.agenda.classes.enums.FromType
import com.kaltt.agenda.classes.events.EventFather
import java.util.*
import kotlin.collections.ArrayList

class MainRepository {
    companion object {
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        private val firebaseAPI = FirebaseAPI.getInstance()

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

        fun setUser(user: FirebaseUser) = firestoreAPI.setUser(ClassAdapter.fromClass.toData(user))

        suspend fun getUsers() {
            //TODO
            firestoreAPI.getUsers()
        }
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        suspend fun fetchEvents() {
            // TODO Revisar ultima actualizacion antes de hacer fetch
            val google = CalendarAPI.fetchEvents()
            //V.googleEvents = if(google.isSuccessful) ClassAdapter.fromData.toClass.asEventFather(google.body()!!) else ArrayList()

            val allEvents = ClassAdapter.fromMap.toData.asEventFather(firestoreAPI.getEvents(firebaseAPI.email))
            V.ownedEvents = ClassAdapter.fromData.toClass.asEventFather(
                FromType.OWNED,
                allEvents.filter { it.owner == firebaseAPI.email }
            )
            V.sharedEvents = ClassAdapter.fromData.toClass.asEventFather(
                FromType.SHARED,
                allEvents.filter { it.sharedWith.contains(firebaseAPI.email) }
            )
        }
        suspend fun saveEvent(e: EventFather) {
            if(e.id.isBlank()) {
                e.id = UUID.randomUUID().toString()
            }
            firestoreAPI.save("events", e.id, ClassAdapter.fromClass.toMap(e))
        }
    }
}