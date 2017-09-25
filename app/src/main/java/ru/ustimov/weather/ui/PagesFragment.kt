package ru.ustimov.weather.ui

import android.os.Bundle
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

class PagesFragment : MvpAppCompatFragment(), PagesView {

    companion object Factory {

        fun create(): PagesFragment = PagesFragment()

    }

    @InjectPresenter
    lateinit var presenter: PagesPresenter

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
        val pager = view?.findViewById(R.id.pager) as ViewPager
        val tabs = view.findViewById(R.id.tabs) as TabLayout
        tabs.setupWithViewPager(pager)

        adapter = PagesViewPagerAdapter(context, childFragmentManager)
        pager.adapter = adapter
    }

    override fun showPages(pages: List<Page>) {
        adapter.pages = pages
    }
}