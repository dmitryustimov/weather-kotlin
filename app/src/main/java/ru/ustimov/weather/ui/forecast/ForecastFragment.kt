package ru.ustimov.weather.ui.forecast

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import ru.ustimov.weather.R
import ru.ustimov.weather.appState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.rx.RxIntent
import ru.ustimov.weather.util.println

class ForecastFragment : MvpAppCompatFragment(), LocationView {

    companion object Factory {

        private const val EXTRA_CITY_ID = "city_id"
        private const val UNKNOWN_CITY_ID = -1L

        private const val REQUEST_CODE_SETTINGS = 1000

        fun create(): ForecastFragment {
            val fragment = ForecastFragment()
            fragment.arguments = Bundle()
            return fragment
        }

        fun create(city: City): ForecastFragment {
            val fragment = ForecastFragment()
            val args = Bundle()
            args.putLong(EXTRA_CITY_ID, city.id())
            fragment.arguments = args
            return fragment
        }

    }

    @InjectPresenter
    lateinit var locationPresenter: LocationPresenter

    private lateinit var rxPermissions: RxPermissions

    @ProvidePresenter
    fun provideLocationPresenter(): LocationPresenter {
        val appState = context.appState()
        val cityId = getCityId()
        return if (cityId != UNKNOWN_CITY_ID) {
            CityLocationPresenter(appState, cityId)
        } else {
            GeoBasedLocationPresenter(appState)
        }
    }

    fun getCityId(): Long = arguments.getLong(EXTRA_CITY_ID, UNKNOWN_CITY_ID)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        rxPermissions = RxPermissions(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_forecast, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun requestAccessCoarseLocationPermission() {
        val appState = context.appState()
        rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError({ it.println(appState.logger) })
                .subscribe(locationPresenter::onAccessCoarseLocationPermissionResult)
    }

    override fun onAccessCoarseLocationGranted() {
        // TODO: request geolocation
        Toast.makeText(context, "Access coarse location has been granted", Toast.LENGTH_SHORT).show()
    }

    override fun showAccessCoarseLocationRationale() {
        Snackbar.make(view!!, R.string.rationale_access_coarse_location, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_ok, { onAccessCoarseLocationRationaleActionClick() })
                .show()
    }

    private fun onAccessCoarseLocationRationaleActionClick() {
        locationPresenter.setViewState(`LocationView$$State`())
        requestAccessCoarseLocationPermission()
    }

    override fun showAccessCoarseLocationDenied() {
        Snackbar.make(view!!, R.string.rationale_access_coarse_location, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_open_settings, { onOpenApplicationDetailsClick() })
                .show()
    }

    private fun onOpenApplicationDetailsClick() {
        locationPresenter.setViewState(`LocationView$$State`())

        val appState = context.appState()
        val data = Uri.parse("package:" + context.packageName)
        val detailsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(data)
        val fallbackIntent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
        RxIntent.resolveActivity(context, detailsIntent)
                .onErrorResumeNext({ RxIntent.resolveActivity(context, fallbackIntent) })
                .subscribeBy(onSuccess = { startActivityForResult(it, REQUEST_CODE_SETTINGS) },
                        onError = { it.println(appState.logger) })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
            when (requestCode) {
                REQUEST_CODE_SETTINGS -> requestAccessCoarseLocationPermission()
                else -> super.onActivityResult(requestCode, resultCode, data)
            }

    override fun onLocationFound(city: City) {
        Toast.makeText(context, "$city", Toast.LENGTH_SHORT).show()
    }

    override fun onLocationNotFound() {
        Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
    }

}