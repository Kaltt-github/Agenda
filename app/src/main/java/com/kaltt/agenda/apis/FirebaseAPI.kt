package com.kaltt.agenda.apis

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAPI private constructor() {
    companion object {
        // Singleton
        private var instance: FirebaseAPI? = null
        fun getInstance(): FirebaseAPI {
            if (this.instance == null) {
                this.instance = FirebaseAPI()
            }
            return this.instance!!
        }
    }

    // Variables <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private val fbAuth = FirebaseAuth.getInstance()
    private val user: FirebaseUser?
        get() = this.fbAuth.currentUser

    // Calculated variables <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    val isSigned: Boolean
        get() = this.user != null
    val email: String
        get() = user?.email ?: ""
    // Functions <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    fun signOut() = fbAuth.signOut()
}