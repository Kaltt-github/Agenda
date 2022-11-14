package com.kaltt.agenda.apis

import com.kaltt.agenda.classes.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class CalendarAPI {
    companion object {
        private fun buildURL(lang: String, location: String): String {
            return "calendars/${lang}.${location}%23holiday@group.v.calendar.google.com/events?key=AIzaSyA6dJ8eTbloDiCK0VPRCa4rtk973cpfFhg"
        }

        interface Fetcher {
            @GET
            fun getEvents(@Url url: String): Call<DataGoogleCalendar>
        }

        fun fetchEvents() : Response<DataGoogleCalendar> = Retrofit
            .Builder()
            .baseUrl("https://www.googleapis.com/calendar/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Fetcher::class.java)
            .getEvents(buildURL("es","ar"))
            .execute()
    }
}