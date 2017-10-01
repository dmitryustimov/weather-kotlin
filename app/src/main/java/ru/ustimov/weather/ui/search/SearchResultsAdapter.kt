package ru.ustimov.weather.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globusltd.recyclerview.Adapter
import com.globusltd.recyclerview.datasource.Datasource
import com.globusltd.recyclerview.view.ClickableViews
import com.globusltd.recyclerview.view.ItemClickHelper
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_search_result.*
import ru.ustimov.weather.R
import ru.ustimov.weather.content.ImageLoader
import ru.ustimov.weather.content.data.SearchResult

class SearchResultsAdapter(
        datasource: Datasource<SearchResult>,
        private val imageLoader: ImageLoader
) : Adapter<SearchResult, SearchResultsAdapter.ViewHolder>(datasource),
        ItemClickHelper.Callback<SearchResult> {

    override fun get(position: Int): SearchResult = datasource[position]

    override fun getClickableViews(position: Int, viewType: Int): ClickableViews = ClickableViews.ITEM_VIEW

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.list_item_search_result, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: SearchResult, position: Int) {
        holder.bindCurrentWeather(item, imageLoader)
    }

    @ContainerOptions(CacheImplementation.SPARSE_ARRAY)
    class ViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        fun bindCurrentWeather(searchResult: SearchResult, imageLoader: ImageLoader) {
            val city = searchResult.city
            cityView.text = city.name()

            val country = searchResult.country
            imageLoader.loadCountryFlag(country, flagView)
            countryView.text = if (country.name().isNullOrEmpty()) country.code() else country.name()

            val weather = searchResult.weather
            temperatureView.text = "${weather.main().temperature()} °C" // TODO: format temperature
        }

    }

}