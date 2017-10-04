package ru.ustimov.weather.content.impl

import android.app.Activity
import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.ustimov.weather.R
import ru.ustimov.weather.content.ImageLoader
import ru.ustimov.weather.content.data.Country
import ru.ustimov.weather.content.impl.glide.GlideApp
import ru.ustimov.weather.content.impl.glide.GlideRequests
import ru.ustimov.weather.content.impl.glide.TabIconTarget

class GlideImageLoader private constructor(
        private val context: Context,
        private val glideRequests: GlideRequests
) : ImageLoader {

    constructor(activity: Activity) : this(activity, GlideApp.with(activity))

    constructor(fragment: Fragment) : this(fragment.context, GlideApp.with(fragment))

    override fun loadCountryFlag(country: Country, imageView: ImageView) {
        val width = context.resources.getDimensionPixelSize(R.dimen.country_flag_width)
        val height = context.resources.getDimensionPixelSize(R.dimen.country_flag_height)
        glideRequests.load(country)
                .override(width, height)
                .placeholder(R.drawable.empty_country_flag)
                .error(R.drawable.empty_country_flag)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }

    override fun loadCountryFlag(country: Country, tab: TabLayout.Tab) {
        val width = context.resources.getDimensionPixelSize(R.dimen.country_flag_width)
        val height = context.resources.getDimensionPixelSize(R.dimen.country_flag_height)
        glideRequests.load(country)
                .placeholder(R.drawable.empty_country_flag)
                .error(R.drawable.empty_country_flag)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(TabIconTarget(tab, width, height))
    }

}