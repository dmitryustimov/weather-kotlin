package ru.ustimov.weather.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import ru.ustimov.weather.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener({
            Log.d("1111", "Selected ${it.title}")
            true
        })
        bottomNavigationView.setOnNavigationItemReselectedListener({
            Log.d("1111", "Reselected ${it.title}")
        })

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.action_my_location
            /*supportFragmentManager.beginTransaction()
                    .replace(R.id.content, PagesFragment.create())
                    .commit()*/
        }
    }

}