package ru.ustimov.weather.content

import android.widget.ImageView
import ru.ustimov.weather.content.data.Country

interface ImageLoader {

    fun loadCountryFlag(country: Country, imageView: ImageView)

}