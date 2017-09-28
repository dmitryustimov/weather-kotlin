package ru.ustimov.weather.content.impl.external.data

import com.google.gson.annotations.SerializedName

internal data class ArrayResponse<out T>(@SerializedName("list") val list: List<T> = emptyList())