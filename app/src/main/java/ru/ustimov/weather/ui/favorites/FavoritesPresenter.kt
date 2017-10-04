package ru.ustimov.weather.ui.favorites

import com.arellomobile.mvp.InjectViewState
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.Favorite
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

    private fun onFavoritesLoaded(favorites: List<Favorite>) {
        viewState.showFavorites(favorites)
        if (favorites.isEmpty()) {
            viewState.showEmpty()
        } else {
            viewState.hideLoading()
        }
    }

}