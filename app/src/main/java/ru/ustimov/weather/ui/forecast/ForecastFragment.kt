package ru.ustimov.weather.ui.forecast

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.ustimov.weather.R
import ru.ustimov.weather.appState
import ru.ustimov.weather.content.data.City

class ForecastFragment : MvpAppCompatFragment(), LocationView {

    companion object Factory {

        private const val EXTRA_CITY_ID = "city_id"
        private const val UNKNOWN_CITY_ID = -1L

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

        if (savedInstanceState == null) {
            rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ Log.d("1111", "$it") })
        }
    }

    private fun requestAccessCoarseLocationPermission() {
        val rationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
        val grantResult = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onLocationFound(city: City) {
        Toast.makeText(context, "$city", Toast.LENGTH_SHORT).show()
    }

    override fun onLocationNotFound() {
        Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
    }

}