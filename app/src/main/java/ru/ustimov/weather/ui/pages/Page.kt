package ru.ustimov.weather.ui.pages

import ru.ustimov.weather.content.data.City

interface Page {

    interface Search : Page

    interface Favorite : Page {

        fun getCity() : City

    }

}