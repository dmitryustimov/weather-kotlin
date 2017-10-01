package ru.ustimov.weather.content.impl

import android.app.Activity
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.ustimov.weather.R
import ru.ustimov.weather.content.ImageLoader
import ru.ustimov.weather.content.data.Country
import ru.ustimov.weather.content.impl.glide.GlideApp
import ru.ustimov.weather.content.impl.glide.GlideRequests

class GlideImageLoader private constructor(private val glideRequests: GlideRequests) : ImageLoader {

    constructor(activity: Activity) : this(glideRequests = GlideApp.with(activity))

    constructor(fragment: Fragment) : this(glideRequests = GlideApp.with(fragment))

    override fun loadCountryFlag(country: Country, imageView: ImageView) {
        glideRequests.load(country)
                .placeholder(R.drawable.empty_country_flag)
                .error(R.drawable.empty_country_flag)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }

}