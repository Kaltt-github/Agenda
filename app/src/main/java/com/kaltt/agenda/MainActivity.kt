package com.kaltt.agenda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kaltt.agenda.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, MainFragment.newInstance())
                .commitNow()
        }
        Tools.checkUser(this)
    }
}