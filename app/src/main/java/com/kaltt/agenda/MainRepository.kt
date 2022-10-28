package com.kaltt.agenda

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.apis.FirebaseAPI
import com.kaltt.agenda.apis.FirestoreAPI
import com.kaltt.agenda.apis.dataClasses.DataUser
import com.kaltt.agenda.classes.Difference
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
        suspend fun getUsers() {
            firestoreAPI.getUsers()
        }
        suspend fun getAllEvents(): ArrayList<Event> {
            var result = ArrayList<Event>()
            // Google events

            // User events (father, children, shared)
            //var owned = firestoreAPI.getOwnedEvents(firebaseAPI.email())
            //owned.forEach { result.addAll(it.allEvents()) }
            //firestoreAPI.getSharedEvents(firebaseAPI.email())
            for (x in 1..180) {
                var mock = EventFather("testing@gmail.com")
                mock.name = "Probando $x"
                mock.color = (x * 2).toDouble()
                //mock.setRepetitions(ScheduleType.WEEKS, 1, LocalDateTime.now().plusMonths(1))
                // ver repeticiones con anti,remind,pospo
                mock.addAnticipation(Difference(days = 1))
                result.addAll(mock.allEvents())
            }
            return result
        }
    }
}