package ru.ustimov.weather.content

import io.reactivex.Flowable
import ru.ustimov.weather.content.data.Suggestion

interface ExternalDatasource {

    fun getSearchSuggestions(query: String): Flowable<out List<Suggestion>>

}