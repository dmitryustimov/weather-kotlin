package ru.ustimov.weather.content.impl.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import ru.ustimov.weather.content.data.Country
import java.io.InputStream

@GlideModule
class OkHttp3GlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(Country::class.java, InputStream::class.java, CountryFlagUrlLoader.Factory())
                .replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
    }

    override fun isManifestParsingEnabled(): Boolean = false

}