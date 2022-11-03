package com.kaltt.agenda

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.apis.Factory
import com.kaltt.agenda.apis.FirebaseAPI
import com.kaltt.agenda.apis.FirestoreAPI
import com.kaltt.agenda.apis.dataClasses.DataUser
import com.kaltt.agenda.classes.EventManager

class MainRepository {
    companion object {
        // Variables <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        private val firebaseAPI = FirebaseAPI.getInstance()
        private val firestoreAPI = FirestoreAPI.getInstance()
        private val eventManager = EventManager.getInstance()
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
            firestoreAPI.getOwnedEvents(firebaseAPI.email())
                .addOnSuccessListener {
                    eventManager.ownedEvents.clear()
                    it.documents.forEach { d -> eventManager.ownedEvents.add(Factory.mapToEvent(d.data!!)) }
                    eventManager.notifyChanges()
                }
            //firestoreAPI.getSharedEvents(firebaseAPI.email())
        }
    }
}