package com.kaltt.agenda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaltt.agenda.classes.EventManager
import com.kaltt.agenda.classes.events.EventFather
import com.kaltt.agenda.ui.main.viewHolders.ListEventAdapter
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    private val coroutineContext: CoroutineContext = newSingleThreadContext("main")
    private val scope = CoroutineScope(coroutineContext)
    private val eventManager = EventManager.getInstance()

    private lateinit var btnSignOut : Button
    private lateinit var btnAddEvent : Button

    private lateinit var rvListEvent : RecyclerView
    private lateinit var adapter : ListEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainRepository.checkUser(this)

        btnSignOut = findViewById(R.id.btn_log_ou)
        btnSignOut.setOnClickListener {
            MainRepository.signOut(this)
        }
        btnAddEvent = findViewById(R.id.btn_add_event)
        btnAddEvent.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }

        rvListEvent = findViewById<RecyclerView>(R.id.rv_list_event)
        rvListEvent.layoutManager = LinearLayoutManager(this)
        adapter = ListEventAdapter(eventManager.allEvents, this)
        eventManager.addObserver(adapter, adapter::notifyDataSetChanged)
        rvListEvent.adapter = adapter
    }
        override fun onStart() {
            super.onStart()
            scope.launch {
                val big = EventFather("emanuel.sileo.2001@gmail.com")
                big.name = "Evento completo"
                big.start = big.start.minusDays(1)
                eventManager.ownedEvents.add(big)
                eventManager.notifyChanges()
                //MainRepository.fetchAllEvents()
            }
        }
}