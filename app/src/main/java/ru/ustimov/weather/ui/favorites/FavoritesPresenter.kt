package ru.ustimov.weather.ui.favorites

import com.arellomobile.mvp.InjectViewState
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.rx.RxMvpPresenter
import ru.ustimov.weather.usecase.QueryFavoritesUsecase

@InjectViewState
class FavoritesPresenter(private val appState: AppState) : RxMvpPresenter<FavoritesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        QueryFavoritesUsecase(appState.repository)
                .run(QueryFavoritesUsecase.Params())
                .doOnSubscribe({ viewState.showLoading() })
                .compose(bindUntilDestroy())
                .observeOn(appState.schedulers.mainThread())
                .subscribe(this::onFavoritesLoaded, {})
    }

    private fun onFavoritesLoaded(cities: List<City>) {
        viewState.showCities(cities)
        if (cities.isEmpty()) {
            viewState.showEmpty()
        } else {
            viewState.hideLoading()
        }
    }

}