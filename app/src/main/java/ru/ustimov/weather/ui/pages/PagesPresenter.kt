package ru.ustimov.weather.ui.pages

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Flowable
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.ui.RxMvpPresenter
import ru.ustimov.weather.util.println

@InjectViewState
class PagesPresenter(private val appState: AppState) :
        RxMvpPresenter<PagesView>() {

    private val searchPage: Page = object : Page.Search {}

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        getPlacePagesOrEmpty()
                .switchMap(this::appendSearchPage)
                .compose(bindUntilDestroy())
                .observeOn(appState.schedulers.mainThread())
                .subscribe({ viewState.showPages(it) }, {})
    }

    private fun getPlacePagesOrEmpty(): Flowable<List<Page>> {
        return appState.repository.getFavorites()
                .observeOn(appState.schedulers.computation())
                .switchMap(this::createPlacePages)
                .doOnError({ it.println(appState.logger) })
                .onErrorResumeNext(Flowable.empty())
    }

    private fun createPlacePages(cities: List<City>): Flowable<List<Page>> {
        return Flowable.fromIterable(cities)
                .map({ createPlacePage(it) })
                .toList()
                .toFlowable()
    }

    private fun createPlacePage(city: City): Page = object : Page.Favorite {
        override fun getCity() = city
    }

    private fun appendSearchPage(favorites: List<Page>): Flowable<List<Page>> {
        return Flowable.just(searchPage)
                .startWith(Flowable.fromIterable(favorites))
                .toList()
                .toFlowable()
    }

}