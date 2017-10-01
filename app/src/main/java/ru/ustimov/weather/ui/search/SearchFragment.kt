package ru.ustimov.weather.ui.search

import android.os.Bundle
import android.support.annotation.Size
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.globusltd.recyclerview.datasource.ListDatasource
import com.globusltd.recyclerview.view.ItemClickHelper
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.fragment_search.*
import ru.ustimov.weather.R
import ru.ustimov.weather.appState
import ru.ustimov.weather.content.data.SearchResult
import ru.ustimov.weather.content.data.Suggestion
import ru.ustimov.weather.content.impl.GlideImageLoader
import ru.ustimov.weather.rx.RxLifecycleFragment
import ru.ustimov.weather.rx.RxSearchView

@ContainerOptions(CacheImplementation.SPARSE_ARRAY)
class SearchFragment : RxLifecycleFragment(), SearchView {

    companion object Factory {

        fun create(): SearchFragment = SearchFragment()

    }

    @InjectPresenter
    lateinit var presenter: SearchPresenter

    private lateinit var suggestionsDatasource: ListDatasource<Suggestion>
    private lateinit var searchResultsDatasource: ListDatasource<SearchResult>

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

        suggestionsDatasource = ListDatasource()
        val suggestionsAdapter = SuggestionsAdapter(suggestionsDatasource)
        searchView.shouldShowProgress = false
        searchView.adapter = suggestionsAdapter
        searchView.setSearchItemAnimator(DefaultItemAnimator())

        val suggestionsRecyclerView = searchView.findViewById<RecyclerView>(R.id.search_recyclerView)
        val suggestionsItemClickHelper = ItemClickHelper<Suggestion>(suggestionsAdapter)
        suggestionsItemClickHelper.setOnItemClickListener({ _, item, _ -> onSuggestionClick(item) })
        suggestionsItemClickHelper.setRecyclerView(suggestionsRecyclerView)

        val imageLoader = GlideImageLoader(this)
        searchResultsDatasource = ListDatasource()
        val searchResultsAdapter = SearchResultsAdapter(searchResultsDatasource, imageLoader)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(SearchResultsItemDecoration(context))
        recyclerView.adapter = searchResultsAdapter
        val searchResultsItemClickHelper = ItemClickHelper<SearchResult>(searchResultsAdapter)
        searchResultsItemClickHelper.setOnItemClickListener({ v, item, position -> onSearchResultClick(v, item, position) })
        searchResultsItemClickHelper.setRecyclerView(recyclerView)
    }

    private fun onSuggestionClick(item: Suggestion): Boolean {
        searchView.setQuery(item.text(), true)
        searchView.hideKeyboard()
        return true
    }

    private fun onSearchResultClick(view: View?, item: SearchResult, position: Int): Boolean =
            when (view?.id) {
            // TODO: R.id.action_add_favorites ->
                else -> presenter.addToFavorites(item.city)
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
        suggestionsDatasource.clear()
        suggestionsDatasource.addAll(suggestions)
        searchView.showSuggestions()
        searchView.setShadow(suggestions.isNotEmpty())
    }

    override fun hideSuggestions() {
        searchView.setShadow(false)
        searchView.hideSuggestions()
        suggestionsDatasource.clear()
    }

    override fun showLoading() = searchView.showProgress()

    override fun showEmpty(query: CharSequence) {
        searchResultsDatasource.clear()

        recyclerView.visibility = View.GONE
        emptyView.visibility = View.VISIBLE

        val placeholder = context.getText(R.string.empty_search_results_for_query_x) as String
        val source = String.format(placeholder, query)
        emptyView.text = Html.fromHtml(source)

        emptyView.action = context.getString(R.string.action_try_again)
        emptyView.onActionButtonClickListener = this::onTryAgainClick
    }

    private fun onTryAgainClick() {
        emptyView.visibility = View.GONE

        searchView.showKeyboard()
        searchView.setQuery("", false)
    }

    override fun showSearchResults(@Size(min = 1) searchResults: List<SearchResult>) {
        searchResultsDatasource.clear()
        searchResultsDatasource.addAll(searchResults)

        emptyView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun showError(throwable: Throwable) {
        emptyView.text = throwable.message
        emptyView.action = null
        emptyView.onActionButtonClickListener = {}
        // TODO: show error
    }

    override fun hideLoading() = searchView.hideProgress()

}