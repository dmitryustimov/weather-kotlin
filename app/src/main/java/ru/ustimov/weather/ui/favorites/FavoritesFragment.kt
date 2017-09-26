package ru.ustimov.weather.ui.favorites

import android.os.Bundle
import android.support.annotation.Size
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.ustimov.weather.R
import ru.ustimov.weather.appState
import ru.ustimov.weather.content.data.City

class FavoritesFragment : MvpAppCompatFragment(), FavoritesView {

    companion object Factory {

        fun create(): FavoritesFragment = FavoritesFragment()

    }

    @InjectPresenter
    lateinit var presenter: FavoritesPresenter

    private lateinit var pagerView: ViewPager
    private lateinit var adapter: FavoritesViewPagerAdapter

    @ProvidePresenter
    fun providePagesPresenter(): FavoritesPresenter {
        val appState = context.appState()
        return FavoritesPresenter(appState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_pages, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerView = view!!.findViewById<ViewPager>(R.id.pager)
        val tabs = view.findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(pagerView)

        adapter = FavoritesViewPagerAdapter(childFragmentManager)
        pagerView.adapter = adapter
    }

    override fun showCities(@Size(min = 0) cities: List<City>) {
        adapter.items = cities
    }

}