package com.kaltt.agenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaltt.agenda.ui.main.viewHolders.ListEventAdapter
import com.kaltt.agenda.classes.Event

class MainActivity : AppCompatActivity() {
    private lateinit var btnSignOut : Button

    private lateinit var rvListEvent : RecyclerView
    private var events = ArrayList<Event>()
    private lateinit var adapter : ListEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainRepository.checkUser(this)

        btnSignOut = findViewById(R.id.btn_log_ou)
        btnSignOut.setOnClickListener {
            MainRepository.signOut(this)
        }

        rvListEvent = findViewById<RecyclerView>(R.id.rv_list_event)
        //rvListEvent.setHasFixedSize(true)
        rvListEvent.layoutManager = LinearLayoutManager(this)
        adapter = ListEventAdapter(events, this)
        rvListEvent.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        events = MainRepository.getUserEvents()
        adapter.notifyDataSetChanged()
    }
}