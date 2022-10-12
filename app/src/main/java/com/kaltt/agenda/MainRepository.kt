package com.kaltt.agenda

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kaltt.agenda.apis.LocalEvents
import com.kaltt.agenda.classes.Event
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository {
    companion object {
        // FIREBASE
        val firebase = FirebaseAuth.getInstance()
        var user: FirebaseUser? = null
        fun isLogged(activity: Activity): Boolean {
            user = firebase.currentUser
            return user != null
        }

        // FIRESTORE
        fun fetchLocalEvents(context: Context, country: String, language: String): ArrayList<Event> {
            val start = "https://www.googleapis.com/calendar/v3/calendars/"
            val values = "${language}.${country}%23holiday@group.v.calendar.google.com/"
            val retrofit = Retrofit.Builder().baseUrl(start+values)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(LocalEvents::class.java)

            val result = api.getLocalEvents().execute()

            return if (result.isSuccessful) {
                result.body()!!
            } else {
                ArrayList<Event>()
            }

        }
    }
}