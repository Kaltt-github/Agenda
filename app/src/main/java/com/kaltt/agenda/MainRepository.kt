package com.kaltt.agenda

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.apis.FirebaseAPI
import com.kaltt.agenda.apis.FirestoreAPI
import com.kaltt.agenda.apis.dataClasses.DataUser
import com.kaltt.agenda.classes.Event
import com.kaltt.agenda.classes.EventFather
import com.kaltt.agenda.classes.EventRepeat
import com.kaltt.agenda.classes.enums.ScheduleType
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime

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
        fun setUser(user: FirebaseUser) {
            firestoreAPI.setUser(DataUser(user.email!!, user.displayName!!))
        }
        fun checkUser(activity : Activity) {
            if(!firebaseAPI.isSigned) {
                activity.startActivity(Intent(activity, LoginActivity::class.java))
                activity.finish()
            }
        }
        fun getUsers() {
            firestoreAPI.getUsers()
        }
        fun getUserEvents(): ArrayList<Event> {
            var r = ArrayList<Event>()
            var event = EventFather()
            event.name = "Evento padre"
            var eventChild = EventRepeat(event, LocalDateTime.now().plusDays(30), false)
            r.add(event)
            r.add(eventChild)
            return r
        }
    }
}