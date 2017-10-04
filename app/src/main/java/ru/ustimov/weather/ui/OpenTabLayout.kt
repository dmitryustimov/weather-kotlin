package ru.ustimov.weather.ui

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet

class OpenTabLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    interface OnTabAddedListener {

        fun onTabAdded(tab: Tab, position: Int)

    }

    var onTabAddedListener: OnTabAddedListener? = null

    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        super.addTab(tab, position, setSelected)
        onTabAddedListener?.onTabAdded(tab, position)
    }

}