package com.kaltt.agenda.apis

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kaltt.agenda.apis.dataClasses.DataUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    fun getUsers() {
        fsAPI.collection("users").get()
    }
}