package ru.ustimov.weather.ui

import android.content.Context
import ru.ustimov.weather.R
import ru.ustimov.weather.content.DatabaseException
import ru.ustimov.weather.content.NetworkException
import ru.ustimov.weather.content.UnknownErrorException

class EmptyViewInfoFactory private constructor() {

    data class Actions(
            val doOnDatabaseException: () -> Unit = {},
            val doOnNetworkException: () -> Unit = {},
            val doOnUnknownException: () -> Unit = {}
    )

    companion object {

        fun fromThrowable(context: Context, throwable: Throwable, actions: Actions): EmptyView.Info =
                when (throwable) {
                    is DatabaseException -> EmptyView.Info(
                            text = context.getText(R.string.message_database_exception),
                            action = context.getText(R.string.action_try_again),
                            listener = actions.doOnDatabaseException)

                    is NetworkException -> EmptyView.Info(
                            text = context.getText(R.string.message_network_exception),
                            action = context.getText(R.string.action_try_again),
                            listener = actions.doOnNetworkException)

                    is UnknownErrorException -> EmptyView.Info(
                            text = context.getText(R.string.message_unknown_exception),
                            action = context.getText(R.string.action_try_again),
                            listener = actions.doOnUnknownException)

                    else -> EmptyView.Info(
                            text = context.getText(R.string.message_unknown_exception),
                            action = context.getText(R.string.action_try_again),
                            listener = actions.doOnUnknownException)
                }

    }

}