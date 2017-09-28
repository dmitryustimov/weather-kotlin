package ru.ustimov.weather.ui.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.globusltd.recyclerview.Adapter
import com.globusltd.recyclerview.datasource.Datasources
import com.globusltd.recyclerview.view.ClickableViews
import com.globusltd.recyclerview.view.ItemClickHelper
import ru.ustimov.weather.R
import ru.ustimov.weather.content.data.Suggestion

class SuggestionsAdapter : Adapter<Suggestion, SuggestionsAdapter.ViewHolder>(Datasources.empty()),
        ItemClickHelper.Callback<Suggestion> {

    override fun get(position: Int): Suggestion = datasource[position]

    override fun getClickableViews(position: Int, viewType: Int): ClickableViews = ClickableViews.ITEM_VIEW

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.list_item_suggestion, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Suggestion, position: Int) =
            holder.bindSuggestion(item)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val iconView: ImageView = itemView.findViewById(R.id.icon)
        private val textView: TextView = itemView.findViewById(R.id.text)

        fun bindSuggestion(suggestion: Suggestion) {
            iconView.setImageResource(if (suggestion.fromHistory()) R.drawable.ic_history else R.drawable.ic_empty)
            textView.text = suggestion.text()
        }

    }

}