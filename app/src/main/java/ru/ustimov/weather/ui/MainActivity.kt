package ru.ustimov.weather.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ru.ustimov.weather.R
import ru.ustimov.weather.ui.favorites.FavoritesFragment
import ru.ustimov.weather.ui.search.SearchFragment

class MainActivity : AppCompatActivity(), FavoritesFragment.Callbacks {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onBottomNavigationItemSelected)
        bottomNavigationView.setOnNavigationItemReselectedListener({})

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.action_my_location
        }
    }

    private fun onBottomNavigationItemSelected(menuItem: MenuItem): Boolean =
            when (menuItem.itemId) {
                R.id.action_my_location -> {
                    showNotImplemented()
                    true
                }
                R.id.action_favorites -> {
                    showFavorites()
                    true
                }
                R.id.action_search -> {
                    showSearch()
                    true
                }
                R.id.action_settings -> {
                    showNotImplemented()
                    true
                }
                else -> false
            }

    private fun showNotImplemented() = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content, NotImplementedFragment.create())
            .commit()

    private fun showFavorites() = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content, FavoritesFragment.create())
            .commit()

    private fun showSearch() = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content, SearchFragment.create())
            .commit()

    override fun onFindCityClick() {
        bottomNavigationView.selectedItemId = R.id.action_search
    }

}