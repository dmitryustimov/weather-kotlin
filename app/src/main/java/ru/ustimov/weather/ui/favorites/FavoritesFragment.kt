package ru.ustimov.weather.ui.favorites

import android.content.Context
import android.os.Bundle
import android.support.annotation.Size
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.ustimov.weather.R
import ru.ustimov.weather.appState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.ui.EmptyView

class FavoritesFragment : MvpAppCompatFragment(), FavoritesView {

    companion object Factory {

        fun create(): FavoritesFragment = FavoritesFragment()

    }

    interface Callbacks {

        fun onFindCityClick()

    }

    @InjectPresenter
    lateinit var presenter: FavoritesPresenter

    private var callbacks: Callbacks? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var pagerView: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var emptyView: EmptyView
    private lateinit var adapter: FavoritesViewPagerAdapter

    @ProvidePresenter
    fun providePagesPresenter(): FavoritesPresenter {
        val appState = context.appState()
        return FavoritesPresenter(appState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater!!.inflate(R.layout.fragment_favorites, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById<ProgressBar>(R.id.progress)

        pagerView = view.findViewById<ViewPager>(R.id.pager)
        tabLayout = view.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(pagerView)

        adapter = FavoritesViewPagerAdapter(childFragmentManager)
        pagerView.adapter = adapter

        emptyView = view.findViewById<EmptyView>(R.id.empty)
        emptyView.onActionButtonClickListener = { callbacks?.onFindCityClick() }
    }

    override fun showLoading() {
        tabLayout.visibility = View.GONE
        pagerView.visibility = View.GONE
        emptyView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun showCities(@Size(min = 0) cities: List<City>) {
        adapter.items = cities
    }

    override fun showEmpty() {
        tabLayout.visibility = View.GONE
        pagerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        emptyView.visibility = View.GONE
        progressBar.visibility = View.GONE
        tabLayout.visibility = View.VISIBLE
        pagerView.visibility = View.VISIBLE
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}