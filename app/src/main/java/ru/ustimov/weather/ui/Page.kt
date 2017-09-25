package ru.ustimov.weather.ui

import ru.ustimov.weather.content.data.City

interface Page {

    interface Search : Page

    interface Place : Page {
        fun getCity() : City
    }

}