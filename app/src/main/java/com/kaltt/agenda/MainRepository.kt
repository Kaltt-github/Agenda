package com.kaltt.agenda

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.apis.Factory
import com.kaltt.agenda.apis.FirebaseAPI
import com.kaltt.agenda.apis.FirestoreAPI
import com.kaltt.agenda.apis.dataClasses.DataUser
import com.kaltt.agenda.classes.Persistent
import com.kaltt.agenda.classes.events.EventFather

class MainRepository {
    companion object {
        // Variables <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        private val firebaseAPI = FirebaseAPI.getInstance()
        private val firestoreAPI = FirestoreAPI.getInstance()
        // Functions <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        fun signOut(activity: Activity) {
            // TODO borrar eventos
            firebaseAPI.signOut()
            // TODO borrar user default
            checkUser(activity)
        }
        fun email(): String = firebaseAPI.email()
        fun setUser(user: FirebaseUser) {
            firestoreAPI.setUser(DataUser(user.email!!, user.displayName!!))
        }
        fun checkUser(activity : Activity) {
            if(!firebaseAPI.isSigned) {
                activity.startActivity(Intent(activity, LoginActivity::class.java))
                activity.finish()
            }
        }
        suspend fun getUsers() {
            firestoreAPI.getUsers()
        }
        suspend fun fetchAllEvents() {
            // Revisar ultima actualizacion antes de hacer fetch
            // Google events
            // TODO
            // User events (father, children, shared)
            V.ownedEvents = firestoreAPI.getOwnedEvents(firebaseAPI.email())
            //firestoreAPI.getSharedEvents(firebaseAPI.email())
        }
        suspend fun saveEvent(e: EventFather) {
            if(e.id.isBlank()) {
                e.id = firestoreAPI.save("events", Factory.eventToMap(e))
            }
            firestoreAPI.update("events", e.id, Factory.eventToMap(e))
        }
    }
}