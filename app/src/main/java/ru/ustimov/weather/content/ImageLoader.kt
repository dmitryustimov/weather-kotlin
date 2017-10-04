package ru.ustimov.weather.content

import android.support.design.widget.TabLayout
import android.widget.ImageView
import ru.ustimov.weather.content.data.Country

interface ImageLoader {

    fun loadCountryFlag(country: Country, imageView: ImageView)

    fun loadCountryFlag(country: Country, tab: TabLayout.Tab)

}