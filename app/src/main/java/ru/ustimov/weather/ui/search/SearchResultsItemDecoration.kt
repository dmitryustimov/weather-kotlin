package ru.ustimov.weather.ui.search

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import ru.ustimov.weather.R

class SearchResultsItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val horizontalOffset: Int = context.resources
            .getDimensionPixelOffset(R.dimen.list_item_search_result_offset_horizontal)

    private val verticalOffset: Int = context.resources
            .getDimensionPixelOffset(R.dimen.list_item_search_result_offset_vertical)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val orientation = getOrientation(parent)
        if (orientation == LinearLayoutManager.VERTICAL) {
            val position = parent.getChildAdapterPosition(view)
            val topOffset = if (position == 0) verticalOffset else 0
            outRect.set(horizontalOffset, topOffset, horizontalOffset, verticalOffset)
        } else {
            TODO("Horizontal layout manager orientation does not supported yet")
        }
    }

    @Throws(IllegalStateException::class)
    private fun getOrientation(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        return (layoutManager as? LinearLayoutManager)?.orientation ?:
                throw IllegalStateException("Use only with a LinearLayoutManager!")
    }

}