package com.kaltt.agenda

import android.app.Activity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.apis.FirebaseAPI
import com.kaltt.agenda.apis.FirestoreAPI
import com.kaltt.agenda.apis.dataClasses.DataUser

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
    }
}