package com.kaltt.agenda

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class Tools {
    companion object {
        private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        fun checkUser(activity : Activity) {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                Log.d("Almanaque", "Usuario no logueado")
                activity.startActivity(Intent(activity, LoginActivity::class.java))
                activity.finish()
            }
        }
    }
}