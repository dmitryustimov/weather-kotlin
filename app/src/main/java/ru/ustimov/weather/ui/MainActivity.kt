package ru.ustimov.weather.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ru.ustimov.weather.R
import ru.ustimov.weather.ui.favorites.FavoritesFragment
import ru.ustimov.weather.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onBottomNavigationItemSelected)

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.action_my_location
        }
    }

    private fun onBottomNavigationItemSelected(menuItem: MenuItem): Boolean =
            when (menuItem.itemId) {
                R.id.action_my_location -> false
                R.id.action_favorites -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.content, FavoritesFragment.create())
                            .commit()
                    true
                }
                R.id.action_search -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.content, SearchFragment.create())
                            .commit()
                    true
                }
                R.id.action_settings -> false
                else -> false
            }

}