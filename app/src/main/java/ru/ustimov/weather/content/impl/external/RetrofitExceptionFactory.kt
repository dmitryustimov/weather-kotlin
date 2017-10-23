package ru.ustimov.weather.content.impl.external

import io.reactivex.Single
import retrofit2.HttpException
import ru.ustimov.weather.content.NetworkException
import ru.ustimov.weather.content.NoConnectionException
import ru.ustimov.weather.content.UnknownErrorException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class RetrofitExceptionFactory private constructor() {

    companion object {

        fun <T> single(throwable: Throwable): Single<T> =
                when (throwable) {
                    is SocketTimeoutException,
                    is UnknownHostException -> Single.error(NoConnectionException(throwable))
                    is HttpException -> Single.error(NetworkException(throwable))
                    else -> Single.error(UnknownErrorException(throwable))
                }

    }

}