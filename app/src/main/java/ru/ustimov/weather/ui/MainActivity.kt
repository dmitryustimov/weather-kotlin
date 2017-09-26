package ru.ustimov.weather.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.ustimov.weather.R
import ru.ustimov.weather.ui.pages.PagesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, PagesFragment.create())
                    .commit()
        }
    }


}