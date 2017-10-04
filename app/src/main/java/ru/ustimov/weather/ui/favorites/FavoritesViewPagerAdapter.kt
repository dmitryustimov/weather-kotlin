package ru.ustimov.weather.ui.favorites

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.ustimov.weather.content.data.Favorite

class FavoritesViewPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    private val _items: MutableList<Favorite> = ArrayList()
    var items: List<Favorite>
        get() = _items
        set(value) {
            _items.clear()
            _items.addAll(value)
            notifyDataSetChanged()
        }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Fragment {
        val favorite = items[position]
        return FavoriteFragment.create(favorite.city)
    }

    override fun getItemPosition(`object`: Any?): Int =
            when (`object`) {
                is FavoriteFragment -> {
                    val index = items.indexOfFirst { it.city.id() == `object`.getCityId() }
                    if (index < 0) POSITION_NONE else index
                }
                else -> super.getItemPosition(`object`)
            }

    override fun getPageTitle(position: Int): CharSequence? {
        val favorite = items[position]
        return favorite.city.name()
    }

}
