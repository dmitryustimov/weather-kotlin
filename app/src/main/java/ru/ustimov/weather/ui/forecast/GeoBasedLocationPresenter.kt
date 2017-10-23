package ru.ustimov.weather.ui.forecast

import android.location.Location
import com.arellomobile.mvp.InjectViewState
import com.tbruyelle.rxpermissions2.Permission
import ru.ustimov.weather.AppState

@InjectViewState
class GeoBasedLocationPresenter(appState: AppState) : LocationPresenter(appState) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.requestAccessCoarseLocationPermission()
    }

    override fun onAccessCoarseLocationPermissionResult(permission: Permission) {
        if (permission.granted) {
            viewState.onAccessCoarseLocationGranted()
        } else {
            if (permission.shouldShowRequestPermissionRationale) {
                viewState.showAccessCoarseLocationRationale()
            } else {
                viewState.showAccessCoarseLocationDenied()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        appState.logger.d("GeoBasedLocationPresenter", "Location=" + location)
    }

}