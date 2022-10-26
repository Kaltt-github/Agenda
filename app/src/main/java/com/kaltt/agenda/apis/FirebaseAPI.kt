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
    private var user: FirebaseUser?
        get() = fbAuth.currentUser
        private set(value) {}

    // Calculated variables <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    var isSigned: Boolean
        get() = this.user != null
        private set(value) {}

    // Functions <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    fun signOut() = fbAuth.signOut()
    fun email(): String = user?.email ?: ""
}