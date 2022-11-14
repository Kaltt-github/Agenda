package com.kaltt.agenda.apis

import com.google.firebase.firestore.FirebaseFirestore
import com.kaltt.agenda.classes.*
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
    suspend fun getUsers() {}
    suspend fun getTags() {}
    suspend fun getEvents(email: String): List<Map<String, Any>> = fsAPI
        .collection("events")
        .whereEqualTo("owner", email)
            // TODO agregar shared
        .get().await()
        .documents.mapNotNull { it.data }

    suspend fun save(collection: String, id: String, x: Map<String, Any>) = fsAPI
        .collection(collection).document(id).set(x)
}