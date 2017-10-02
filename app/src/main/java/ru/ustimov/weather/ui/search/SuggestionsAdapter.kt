package ru.ustimov.weather.ui.search

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.globusltd.recyclerview.Adapter
import com.globusltd.recyclerview.datasource.Datasource
import com.globusltd.recyclerview.view.ClickableViews
import com.globusltd.recyclerview.view.ItemClickHelper
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_suggestion.*
import ru.ustimov.weather.R
import ru.ustimov.weather.content.data.Suggestion

class SuggestionsAdapter(datasource: Datasource<Suggestion>) :
        Adapter<Suggestion, SuggestionsAdapter.ViewHolder>(datasource),
        ItemClickHelper.Callback<Suggestion> {

    var query: String = ""

    override fun get(position: Int): Suggestion = datasource[position]

    override fun getClickableViews(position: Int, viewType: Int): ClickableViews = ClickableViews.ITEM_VIEW

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.list_item_suggestion, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Suggestion, position: Int) =
            holder.bindSuggestion(query, item)

    @ContainerOptions(CacheImplementation.SPARSE_ARRAY)
    class ViewHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {

        fun bindSuggestion(query: String, suggestion: Suggestion) {
            iconView.setImageResource(if (suggestion.fromHistory()) R.drawable.ic_history else R.drawable.ic_empty)

            val text = suggestion.text()
            val lowercaseText = text.toLowerCase()
            val lowercaseQuery = query.toLowerCase()
            if (lowercaseText.contains(lowercaseQuery) && !query.isEmpty()) {
                val s = SpannableString(text)
                val start = lowercaseText.indexOf(lowercaseQuery)
                val end = start + query.length
                s.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                textView.setText(s, TextView.BufferType.SPANNABLE)
            } else {
                textView.text = text
            }
        }

    }

}