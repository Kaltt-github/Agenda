package com.kaltt.agenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kaltt.agenda.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var btn_logout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, MainFragment.newInstance())
                .commitNow()
        }
        btn_logout = findViewById(R.id.btn_log_ou)
        btn_logout.setOnClickListener {
            MainRepository.signOut(this)
        }
        MainRepository.checkUser(this)
        //var resultados = MainRepository.fetchLocalEvents(this, "ar","es")
    }
}