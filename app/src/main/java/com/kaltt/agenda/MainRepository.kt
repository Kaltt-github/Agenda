package com.kaltt.agenda

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainRepository {
    companion object {
        val firebase = FirebaseAuth.getInstance()
        var user : FirebaseUser? = null
        fun isLogged(activity: Activity): Boolean {
            user = firebase.currentUser
            return user != null
        }
    }
}