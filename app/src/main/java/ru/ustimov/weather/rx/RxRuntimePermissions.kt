package ru.ustimov.weather.rx

import android.app.Application
import android.content.Context
import java.util.concurrent.atomic.AtomicLong

class RxRuntimePermissions(context: Context) {

    private companion object {
        private const val KEY_RX_RUNTIME_PERMISSIONS = "key_rx_runtime_permissions"
        private val COUNTER = AtomicLong(0)
    }

    init {
        val application = context.applicationContext as Application
        //application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

}