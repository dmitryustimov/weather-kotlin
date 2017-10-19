package ru.ustimov.weather.ui.forecast

import android.location.Location
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

    override fun onLocationChanged(location: Location) {
        throw IllegalStateException("Presenter does not support location changes")
    }

}