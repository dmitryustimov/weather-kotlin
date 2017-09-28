package ru.ustimov.weather.ui.search

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.globusltd.recyclerview.datasource.Datasources
import com.globusltd.recyclerview.datasource.ListDatasource
import com.globusltd.recyclerview.view.ItemClickHelper
import ru.ustimov.weather.R
import ru.ustimov.weather.appState
import ru.ustimov.weather.content.data.Suggestion
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

        val recyclerView = searchView.findViewById<RecyclerView>(R.id.search_recyclerView)
        val itemClickHelper = ItemClickHelper<Suggestion>(suggestionsAdapter)
        itemClickHelper.setOnItemClickListener({ _, item, _ -> onSuggestionClick(item) })
        itemClickHelper.setRecyclerView(recyclerView)
    }

    private fun onSuggestionClick(item: Suggestion): Boolean {
        searchView.setQuery(item.text(), true)
        searchView.hideKeyboard()
        return true
    }

    override fun onResume() {
        super.onResume()
        RxSearchView.onQueryTextChanged(searchView)
                .doOnNext({ if (it.submit) searchView.hideKeyboard() })
                .doOnNext({ presenter.onQueryChanged(it.query, it.submit) })
                .compose(bindUntil(Event.PAUSE))
                .subscribeOn(context.appState().schedulers.mainThread())
                .subscribe()
    }

    override fun showSuggestions(suggestions: List<Suggestion>) {
        val datasource = ListDatasource<Suggestion>(suggestions)
        suggestionsAdapter.swap(datasource)
        searchView.showSuggestions()
    }

    override fun hideSuggestions() {
        searchView.hideSuggestions()
        suggestionsAdapter.swap(Datasources.empty())
    }

    override fun showLoading() = searchView.showProgress()

    override fun hideLoading() = searchView.hideProgress()

}