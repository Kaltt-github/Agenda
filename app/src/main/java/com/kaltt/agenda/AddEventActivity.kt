package com.kaltt.agenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kaltt.agenda.classes.events.EventFather

class AddEventActivity() : AppCompatActivity() {
    private lateinit var event: EventFather

    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        btnAdd = findViewById(R.id.btn_complete_event)
        btnAdd.setOnClickListener {
            finish()
        }
    }
    override fun onStart() {
        super.onStart()
        event = EventFather(MainRepository.email())
        // TODO Assign values to visual screen
    }
}