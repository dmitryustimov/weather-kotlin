package ru.ustimov.weather.content

import android.app.Activity
import android.support.v4.app.Fragment
import ru.ustimov.weather.content.impl.GlideImageLoader

class ImageLoaderFactory private constructor() {

    companion object {

        fun create(activity: Activity) = GlideImageLoader(activity)

        fun create(fragment: Fragment) = GlideImageLoader(fragment)

    }

}