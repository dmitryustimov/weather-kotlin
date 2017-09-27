package ru.ustimov.weather.ui.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.globusltd.recyclerview.datasource.Datasources
import com.globusltd.recyclerview.datasource.ListDatasource
import com.globusltd.recyclerview.view.ItemClickHelper
import ru.ustimov.weather.R
import ru.ustimov.weather.appState
import ru.ustimov.weather.rx.RxLifecycleFragment
import ru.ustimov.weather.rx.RxSearchView

class SearchFragment : RxLifecycleFragment(), SearchView {

    companion object Factory {

        fun create(): SearchFragment = SearchFragment()

    }

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    private lateinit var searchView: com.lapism.searchview.SearchView
    private lateinit var suggestionsAdapter: SuggestionsAdapter

    @ProvidePresenter
    fun provideSearchPresenter(): SearchPresenter {
        val appState = context.appState()
        return SearchPresenter(appState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater!!.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        suggestionsAdapter = SuggestionsAdapter()

        searchView = view!!.findViewById(R.id.search_view)
        searchView.shouldShowProgress = false
        searchView.adapter = suggestionsAdapter
        val animator = DefaultItemAnimator()
        animator.supportsChangeAnimations = false
        searchView.setSearchItemAnimator(animator)

        /*searchView.setOnQueryTextListener(object : com.lapism.searchview.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    suggestionsAdapter.swap(Datasources.empty())
                    searchView.hideProgress()
                    searchView.hideSuggestions()
                } else {
                    val datasource = ListDatasource<Any>()
                    datasource.add("Item 1")
                    datasource.add("Item 2")
                    suggestionsAdapter.swap(datasource)
                    searchView.showSuggestions()
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "q=$query", Toast.LENGTH_SHORT).show()
                searchView.hideProgress()
                searchView.hideSuggestions()
                searchView.hideKeyboard()
                return true
            }

        })*/

        val recyclerView = searchView.findViewById<RecyclerView>(R.id.search_recyclerView)
        val itemClickHelper = ItemClickHelper(suggestionsAdapter)
        itemClickHelper.setOnItemClickListener({ _, item, _ ->
            searchView.setQuery(item as CharSequence, true)
            searchView.hideKeyboard()
            true
        })
        itemClickHelper.setRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        RxSearchView.onQueryText(searchView)
                .doOnNext({
                    if (it.submit) {
                        presenter.onQueryTextSubmit(it.query)
                    } else {
                        presenter.onQueryTextChanged(it.query)
                    }
                })
                .compose(bindUntil(Event.PAUSE))
                .subscribeOn(context.appState().schedulers.mainThread())
                .subscribe()
    }

}