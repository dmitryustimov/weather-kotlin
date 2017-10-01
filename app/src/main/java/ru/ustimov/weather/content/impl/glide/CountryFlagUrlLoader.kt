package ru.ustimov.weather.content.impl.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import ru.ustimov.weather.content.data.Country
import java.io.InputStream

internal class CountryFlagUrlLoader(
        modelLoader: ModelLoader<GlideUrl, InputStream>,
        modelCache: ModelCache<Country, GlideUrl>
) : BaseGlideUrlLoader<Country>(modelLoader, modelCache) {

    override fun getUrl(country: Country, width: Int, height: Int, options: Options?): String {
        val minWidth = when (width) {
            in 0..100 -> 100
            in 101..250 -> 250
            else -> 1000
        }
        return "https://github.com/hjnilsson/country-flags/raw/master/png${minWidth}px/${country.code()}.png"
    }

    override fun handles(model: Country): Boolean = true

    internal class Factory : ModelLoaderFactory<Country, InputStream> {

        private val modelCache = ModelCache<Country, GlideUrl>(20)

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<Country, InputStream> {
            val modelLoader = multiFactory.build(GlideUrl::class.java, InputStream::class.java)
            return CountryFlagUrlLoader(modelLoader, modelCache)
        }

        override fun teardown() {
        }

    }

}