package ru.ustimov.weather.ui.forecast

import android.location.Location
import com.arellomobile.mvp.InjectViewState
import com.tbruyelle.rxpermissions2.Permission
import io.reactivex.Flowable
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.rx.RxMvpPresenter
import ru.ustimov.weather.usecase.QueryCityUsecase

@InjectViewState
abstract class LocationPresenter(protected val appState: AppState) : RxMvpPresenter<LocationView>() {

    private val queryCityUsecase = QueryCityUsecase(appState.repository)

    protected fun queryCityById(cityId: Long): Flowable<out City> =
            queryCityUsecase.run(QueryCityUsecase.Params(cityId))
                    .observeOn(appState.schedulers.mainThread())
                    .compose(bindUntilDestroy())

    open fun onAccessCoarseLocationPermissionResult(permission: Permission) {
    }

    abstract fun onLocationChanged(location: Location)

}