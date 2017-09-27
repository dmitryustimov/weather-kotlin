package ru.ustimov.weather.ui.search

import com.arellomobile.mvp.InjectViewState
import ru.ustimov.weather.AppState
import ru.ustimov.weather.rx.RxMvpPresenter

@InjectViewState
class SearchPresenter(private val appState: AppState) : RxMvpPresenter<SearchView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }

}