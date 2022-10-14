package com.kaltt.agenda.apis

import com.kaltt.agenda.classes.Event
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarAPI {
    @GET("/events")
    fun getLocalEvents(@Query("key") key: String = "AIzaSyA6dJ8eTbloDiCK0VPRCa4rtk973cpfFhg") : Call<ArrayList<Event>>
}