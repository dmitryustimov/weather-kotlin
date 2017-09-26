package ru.ustimov.weather.ui.favorites

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.ustimov.weather.content.data.City

class FavoriteFragment : MvpAppCompatFragment() {

    companion object Factory {

        private const val EXTRA_CITY_ID = "city_id"

        fun create(city : City): FavoriteFragment {
            val fragment = FavoriteFragment()
            val args = Bundle()
            args.putLong(EXTRA_CITY_ID, city.id())
            fragment.arguments = args;
            return fragment
        }

    }

    fun getCityId(): Long = arguments.getLong(EXTRA_CITY_ID)

}