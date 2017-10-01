package ru.ustimov.weather.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.ustimov.weather.R
import ru.ustimov.weather.content.data.City

class FavoriteFragment : MvpAppCompatFragment() {

    companion object Factory {

        private const val EXTRA_CITY_ID = "city_id"

        fun create(city: City): FavoriteFragment {
            val fragment = FavoriteFragment()
            val args = Bundle()
            args.putLong(EXTRA_CITY_ID, city.id())
            fragment.arguments = args;
            return fragment
        }

    }

    fun getCityId(): Long = arguments.getLong(EXTRA_CITY_ID)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_favorite, container, false)

}