package ru.ustimov.weather.ui.pages

import android.os.Bundle
import android.support.annotation.Size
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.ustimov.weather.R
import ru.ustimov.weather.appState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.content.data.Location

class PagesFragment : MvpAppCompatFragment(), PagesView {

    companion object Factory {

        fun create(): PagesFragment = PagesFragment()

    }

    @InjectPresenter
    lateinit var presenter: PagesPresenter

    private lateinit var pagerView: ViewPager
    private lateinit var adapter: PagesViewPagerAdapter

    @ProvidePresenter
    fun providePagesPresenter(): PagesPresenter {
        val appState = context.appState()
        return PagesPresenter(appState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_pages, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerView = view!!.findViewById<ViewPager>(R.id.pager)
        val tabs = view.findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(pagerView)

        adapter = PagesViewPagerAdapter(context, childFragmentManager)
        pagerView.adapter = adapter

        view.findViewById<Button>(R.id.button).setOnClickListener({
            val city = object : City {
                override fun id(): Long = 520555L
                override fun name(): String? = "Nizhniy Novgorod"
                override fun country(): String? = "RU"
                override fun location(): Location = object : Location {
                    override fun latitude(): Double = 56.3287
                    override fun longitude(): Double = 44.002
                }
            }
            context.appState().repository.addFavorite(city).subscribe()

        })
    }

    override fun showPages(@Size(min = 0) pages: List<Page>) {
        adapter.items = pages
        pagerView.setCurrentItem(0, true)
    }

}