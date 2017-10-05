package ru.ustimov.weather.ui

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import ru.ustimov.weather.R

class OpenTabLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    interface OnTabAddedListener {

        fun onTabAdded(tab: Tab, position: Int)

    }

    var onTabAddedListener: OnTabAddedListener? = null

    private val tabLayout: Int

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.OpenTabLayout,
                defStyleAttr, android.support.design.R.style.Widget_Design_TabLayout)
        try {
            tabLayout = a.getResourceId(R.styleable.OpenTabLayout_tabLayout, 0)
        } finally {
            a.recycle()
        }
    }

    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        if (tabLayout != 0) {
            tab.customView = LayoutInflater.from(context).inflate(tabLayout, this, false)
        }
        super.addTab(tab, position, setSelected)
        onTabAddedListener?.onTabAdded(tab, position)
    }

}