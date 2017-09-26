package ru.ustimov.weather.ui.pages

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.ustimov.weather.R
import ru.ustimov.weather.ui.pages.city.FavoriteFragment
import ru.ustimov.weather.ui.pages.search.SearchFragment

class PagesViewPagerAdapter(val context: Context, fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    private val _items: MutableList<Page> = ArrayList()
    var items: List<Page>
        get() = _items
        set(value) {
            _items.clear()
            _items.addAll(value)
            notifyDataSetChanged()
        }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Fragment {
        val page = items[position]
        return when (page) {
            is Page.Search -> SearchFragment.create()
            is Page.Favorite -> FavoriteFragment.create(page.getCity())
            else -> throw IllegalArgumentException("Unsupported page type")
        }
    }

    override fun getItemPosition(`object`: Any?): Int =
            when (`object`) {
                is SearchFragment -> {
                    val index = items.indexOfLast { it is Page.Search }
                    if (index < 0) POSITION_NONE else index
                }
                is FavoriteFragment -> {
                    val index = items.indexOfFirst {
                        it is Page.Favorite && it.getCity().id() == `object`.getCityId()
                    }
                    if (index < 0) POSITION_NONE else index
                }
                else -> super.getItemPosition(`object`)
            }

    override fun getPageTitle(position: Int): CharSequence? {
        val page = items[position]
        return when (page) {
            is Page.Search -> context.getText(R.string.fragment_search_title)
            is Page.Favorite -> page.getCity().name()
            else -> super.getPageTitle(position)
        }
    }

}
