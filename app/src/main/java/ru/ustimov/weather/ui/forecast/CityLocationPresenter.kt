package ru.ustimov.weather.ui.forecast

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Flowable
import ru.ustimov.weather.AppState

@InjectViewState
class CityLocationPresenter(appState: AppState, private val cityId: Long) : LocationPresenter(appState) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        queryCityById(cityId)
                .doOnError({ viewState.onLocationNotFound() })
                .onErrorResumeNext(Flowable.empty())
                .subscribe({ viewState.onLocationFound(it) })
    }

}