package ru.ustimov.weather.ui.search

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatDrawableManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.globusltd.recyclerview.Adapter
import com.globusltd.recyclerview.datasource.Datasource
import com.globusltd.recyclerview.datasource.Datasources
import com.globusltd.recyclerview.diff.DiffCallback
import com.globusltd.recyclerview.diff.DiffCallbackFactory
import com.globusltd.recyclerview.diff.SimpleDatasourcesDiffCallback
import com.globusltd.recyclerview.view.ClickableViews
import com.globusltd.recyclerview.view.ItemClickHelper
import ru.ustimov.weather.R
import ru.ustimov.weather.content.data.Suggestion

class SuggestionsAdapter : Adapter<Suggestion, SuggestionsAdapter.ViewHolder>(Datasources.empty(),
        SuggestionsDiffCallbackFactory()), ItemClickHelper.Callback<Suggestion> {

    override fun get(position: Int): Suggestion = datasource[position]

    override fun getClickableViews(position: Int, viewType: Int): ClickableViews = ClickableViews.ITEM_VIEW

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.search_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Suggestion, position: Int) =
            holder.bindSuggestion(item)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val iconView: ImageView = itemView.findViewById(R.id.search_icon)
        private val textView: TextView = itemView.findViewById(R.id.search_text)
        private val historyDrawable: Drawable = AppCompatDrawableManager.get()
                .getDrawable(itemView.context, R.drawable.ic_history)

        init {
            textView.setTextColor(Color.BLACK)
        }

        fun bindSuggestion(suggestion: Suggestion) {
            iconView.setImageDrawable(if (suggestion.fromHistory()) historyDrawable else null)
            textView.text = suggestion.getText()
        }

    }

    private class SuggestionsDiffCallbackFactory : DiffCallbackFactory<Suggestion> {

        override fun createDiffCallback(oldDatasource: Datasource<out Suggestion>,
                                        newDatasource: Datasource<out Suggestion>): DiffCallback =
                object : SimpleDatasourcesDiffCallback<Suggestion>(oldDatasource, newDatasource) {

                    override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean =
                            oldItem == newItem

                    override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean =
                            TextUtils.equals(oldItem.getText(), newItem.getText()) &&
                                    oldItem.fromHistory() == newItem.fromHistory()

                }

    }

}