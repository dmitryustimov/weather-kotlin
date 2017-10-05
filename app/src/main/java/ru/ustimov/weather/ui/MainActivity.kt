package ru.ustimov.weather.ui

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.activity_main.*
import ru.ustimov.weather.R
import ru.ustimov.weather.ui.favorites.FavoritesFragment
import ru.ustimov.weather.ui.forecast.ForecastFragment
import ru.ustimov.weather.ui.search.SearchFragment

@ContainerOptions(CacheImplementation.SPARSE_ARRAY)
class MainActivity : AppCompatActivity(), FavoritesFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationBar.setOnNavigationItemSelectedListener(this::onBottomNavigationItemSelected)
        bottomNavigationBar.setOnNavigationItemReselectedListener({})
        if (savedInstanceState == null) {
            bottomNavigationBar.selectedItemId = R.id.action_my_location
        }
    }

    private fun onBottomNavigationItemSelected(menuItem: MenuItem): Boolean =
            when (menuItem.itemId) {
                R.id.action_my_location -> {
                    showForecast()
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

    private fun showForecast() = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content, ForecastFragment.create())
            .commit()

    private fun showFavorites() = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content, FavoritesFragment.create())
            .commit()

    private fun showSearch() = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content, SearchFragment.create())
            .commit()

    private fun showNotImplemented() = supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.content, NotImplementedFragment.create())
            .commit()

    override fun onFindCityClick() {
        bottomNavigationBar.selectedItemId = R.id.action_search
    }

}