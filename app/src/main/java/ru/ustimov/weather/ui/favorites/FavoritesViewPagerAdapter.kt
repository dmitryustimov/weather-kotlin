package ru.ustimov.weather.ui.favorites

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.ustimov.weather.content.data.City

class FavoritesViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    private val _items: MutableList<City> = ArrayList()
    var items: List<City>
        get() = _items
        set(value) {
            _items.clear()
            _items.addAll(value)
            notifyDataSetChanged()
        }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Fragment {
        val city = items[position]
        return FavoriteFragment.create(city)
    }

    override fun getItemPosition(`object`: Any?): Int =
            when (`object`) {
                is FavoriteFragment -> {
                    val index = items.indexOfFirst { it.id() == `object`.getCityId() }
                    if (index < 0) POSITION_NONE else index
                }
                else -> super.getItemPosition(`object`)
            }

    override fun getPageTitle(position: Int): CharSequence? {
        val city = items[position]
        return city.name()
    }

}
