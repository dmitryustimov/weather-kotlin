package ru.ustimov.weather.ui

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Flowable
import ru.ustimov.weather.AppState
import ru.ustimov.weather.content.data.City
import ru.ustimov.weather.util.println

@InjectViewState
class PagesPresenter(private val appState: AppState) :
        RxMvpPresenter<PagesView>() {

    private val searchPage: Page = object : Page.Search {}

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Flowable.just(searchPage).startWith(getCitiesOrEmpty())
                .observeOn(appState.schedulers.computation())
                .toList()
                .compose(bindUntilDestroy())
                .observeOn(appState.schedulers.mainThread())
                .subscribe({ viewState.showPages(it) }, {})
    }

    private fun getCitiesOrEmpty(): Flowable<Page> {
        return appState.repository.getCities()
                .observeOn(appState.schedulers.computation())
                .map({ createPlacePage(it) })
                .doOnError({ it.println(appState.logger) })
                .onErrorResumeNext(Flowable.empty())
    }

    private fun createPlacePage(city: City): Page = object : Page.Place {
        override fun getCity() = city
    }

}