package ru.ustimov.weather.ui.search

import android.graphics.Color
import android.support.v7.widget.RecyclerView
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

class SuggestionsAdapter : Adapter<Any, SuggestionsAdapter.ViewHolder>(Datasources.empty(),
        SuggestionsDiffCallbackFactory()), ItemClickHelper.Callback<Any> {

    override fun get(position: Int): Any = datasource[position]

    override fun getClickableViews(position: Int, viewType: Int): ClickableViews = ClickableViews.ITEM_VIEW

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.search_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Any, position: Int) {
        if (item is CharSequence) {
            holder.textView.setTextColor(Color.BLACK)
            holder.textView.text = item
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val iconView: ImageView = itemView.findViewById(R.id.search_icon)
        val textView: TextView = itemView.findViewById(R.id.search_text)

    }

    private class SuggestionsDiffCallbackFactory : DiffCallbackFactory<Any> {

        override fun createDiffCallback(oldDatasource: Datasource<out Any>,
                                        newDatasource: Datasource<out Any>): DiffCallback =
                object : SimpleDatasourcesDiffCallback<Any>(oldDatasource, newDatasource) {

                    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        return false
                    }

                    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        return false
                    }

                }

    }

}