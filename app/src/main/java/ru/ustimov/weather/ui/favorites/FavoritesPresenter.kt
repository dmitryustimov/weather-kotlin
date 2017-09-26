package ru.ustimov.weather.ui.favorites

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Flowable
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.ui.RxMvpPresenter
import ru.ustimov.weather.util.println

@InjectViewState
class FavoritesPresenter(private val appState: AppState) :
        RxMvpPresenter<FavoritesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        getFavoritesOrEmpty()
                .compose(bindUntilDestroy())
                .observeOn(appState.schedulers.mainThread())
                .subscribe({ viewState.showCities(it) }, {}) // TODO: empty view
    }

    private fun getFavoritesOrEmpty(): Flowable<out List<City>> {
        return appState.repository.getFavorites()
                .doOnError({ it.println(appState.logger) })
                .onErrorResumeNext(Flowable.empty())
    }

}