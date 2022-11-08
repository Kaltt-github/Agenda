package com.kaltt.agenda.apis

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kaltt.agenda.apis.dataClasses.DataUser
import com.kaltt.agenda.classes.enums.FromType
import com.kaltt.agenda.classes.events.EventFather
import kotlinx.coroutines.tasks.await

class FirestoreAPI private constructor() {
    companion object {
        // Singleton
        private var instance: FirestoreAPI? = null
        fun getInstance(): FirestoreAPI {
            if(this.instance == null) {
                this.instance = FirestoreAPI()
            }
            return this.instance!!
        }
    }
    // Variables <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private val fsAPI = FirebaseFirestore.getInstance()
    // Calculated variables <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    // Functions <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    fun setUser(user: DataUser) {
        fsAPI.collection("users").document(user.email).set(user)
    }
    suspend fun getUsers(): ArrayList<Map<String, String>> {
        var r = ArrayList<Map<String, String>>()
        fsAPI.collection("users").get().addOnSuccessListener {
            var x = it.documents.map { d -> d.data }
            r = x as ArrayList<Map<String, String>>
        }
        return r
    }
    suspend fun getOwnedEvents(email: String): ArrayList<EventFather> {
        return Factory.mapsToEvents(
            FromType.OWNED,
            fsAPI.collection("events")
            .whereEqualTo("owner",email)
            .get().await()
            .documents.map { it.data }
        )
    }

    suspend fun getSharedEvents(email: String): ArrayList<EventFather> {
        return ArrayList()
    }
}