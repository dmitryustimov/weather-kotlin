package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.SerializedName
import ru.ustimov.weather.content.data.Country

internal data class RestCountriesCountry(
        @SerializedName("alpha2Code") private val code: String,
        @SerializedName("name") private val name: String
) : Country {

    override fun code() = code.toLowerCase()

    override fun name() = name

}