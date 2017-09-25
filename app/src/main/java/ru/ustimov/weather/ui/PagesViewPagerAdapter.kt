package ru.ustimov.weather.ui

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.ustimov.weather.R

class PagesViewPagerAdapter(val context: Context, fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    private val _pages: MutableList<Page> = ArrayList()
    var pages: List<Page>
        get() = _pages
        set(value) {
            _pages.clear()
            _pages.addAll(value)
            notifyDataSetChanged()
        }

    override fun getCount(): Int = pages.size

    override fun getItem(position: Int): Fragment =
            when (pages[position]) {
                is Page.Search -> SearchFragment.create()
                is Page.Place -> SearchFragment.create()
                else -> throw IllegalArgumentException("Unsupported page type")
            }

    override fun getItemPosition(`object`: Any?): Int =
            when (`object`) {
                is SearchFragment -> {
                    val index = pages.indexOfLast { it is Page.Search }
                    if (index == -1) POSITION_NONE else index
                }
                // is Page.Place ->
                else -> super.getItemPosition(`object`)
            }

    override fun getPageTitle(position: Int): CharSequence? =
            when (pages[position]) {
                is Page.Search -> context.getText(R.string.fragment_search_title)
                // is Page.Place ->
                else -> super.getPageTitle(position)
            }

}
