package ru.ustimov.weather.content.impl

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import ru.ustimov.weather.util.Logger
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

internal class NetworkScheduler(private val context: Context,
                       private val logger: Logger,
                       autoStart: Boolean = true) : Scheduler() {

    private companion object {

        private val TAG: String = "NetworkScheduler"

        private val DEFAULT_THREAD_COUNT: Int = 5
        private val DEFAULT_KEEP_ALIVE_TIME: Long = 15L
        private val DEFAULT_KEEP_ALIVE_TIME_UNIT: TimeUnit = TimeUnit.SECONDS
        private val POOL_NUMBER: AtomicInteger = AtomicInteger(1)

    }

    private val executor: ThreadPoolExecutor
    private val scheduler: Scheduler
    private val connectivityManager: ConnectivityManager
    private val receiver: NetworkBroadcastReceiver

    init {
        val poolNumber = POOL_NUMBER.getAndIncrement()
        executor = ThreadPoolExecutor(DEFAULT_THREAD_COUNT, DEFAULT_THREAD_COUNT,
                DEFAULT_KEEP_ALIVE_TIME, DEFAULT_KEEP_ALIVE_TIME_UNIT,
                LinkedBlockingQueue<Runnable>(), NetworkThreadFactory(poolNumber))
        scheduler = Schedulers.from(executor)

        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        receiver = NetworkBroadcastReceiver()
        if (autoStart) {
            start()
        }
    }

    private fun adjustThreadsCount(networkInfo: NetworkInfo?) {
        if (networkInfo == null || !networkInfo.isConnectedOrConnecting) {
            setThreadCount(DEFAULT_THREAD_COUNT)
        } else {
            val threadCount = getThreadCountFromNetworkInfo(networkInfo)
            setThreadCount(threadCount)
        }
    }

    private fun getThreadCountFromNetworkInfo(networkInfo: NetworkInfo): Int {
        when (networkInfo.type) {
            ConnectivityManager.TYPE_WIFI,
            ConnectivityManager.TYPE_WIMAX,
            ConnectivityManager.TYPE_ETHERNET -> return 4

            ConnectivityManager.TYPE_MOBILE -> {
                when (networkInfo.subtype) {
                    TelephonyManager.NETWORK_TYPE_LTE,
                    TelephonyManager.NETWORK_TYPE_HSPAP,
                    TelephonyManager.NETWORK_TYPE_EHRPD -> return 3

                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_CDMA,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_EVDO_B -> return 2

                    TelephonyManager.NETWORK_TYPE_GPRS,
                    TelephonyManager.NETWORK_TYPE_EDGE -> return 1

                    else -> return DEFAULT_THREAD_COUNT
                }
            }
            else -> return DEFAULT_THREAD_COUNT
        }
    }

    private fun setThreadCount(threadCount: Int) {
        executor.corePoolSize = threadCount
        executor.maximumPoolSize = threadCount
        logger.d(TAG, "Threads count has changed to $threadCount")
    }

    override fun start() {
        super.start()
        receiver.subscribe(context, logger)
    }

    override fun createWorker(): Worker = scheduler.createWorker()

    override fun shutdown() {
        super.shutdown()
        receiver.unsubscribe(context)
        executor.shutdown()
    }

    inner class NetworkBroadcastReceiver : BroadcastReceiver() {

        private var networkInfo: NetworkInfo? = null

        internal fun subscribe(context: Context, logger: Logger) {
            val intentFilter = IntentFilter()
            val scansNetworkChanges = hasPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)
            if (scansNetworkChanges) {
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            } else {
                logger.w(TAG, "Declare permission ${Manifest.permission.ACCESS_NETWORK_STATE} in AndroidManifest.xml " +
                        "to automatically adjust network threads count")
            }
            context.registerReceiver(receiver, intentFilter)
        }

        private fun hasPermission(context: Context, permission: String): Boolean =
                (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)

        override fun onReceive(context: Context?, intent: Intent?) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            if (this.networkInfo != networkInfo) {
                this.networkInfo = networkInfo
                adjustThreadsCount(networkInfo)
            }
        }

        internal fun unsubscribe(context: Context) {
            context.unregisterReceiver(receiver)
            networkInfo = null
        }

    }

    private class NetworkThreadFactory(poolNumber: Int) : ThreadFactory {

        private val group: ThreadGroup
        private val threadNumber: AtomicInteger
        private val namePrefix: String

        init {
            val securityManager = System.getSecurityManager()
            group = if (securityManager != null) {
                securityManager.threadGroup
            } else {
                Thread.currentThread().threadGroup
            }
            threadNumber = AtomicInteger(1)
            namePrefix = "network-scheduler-$poolNumber-thread-"
        }

        override fun newThread(runnable: Runnable?): Thread {
            val name = namePrefix + threadNumber.getAndIncrement()
            val thread = Thread(group, runnable, name, 0)
            thread.priority = Thread.NORM_PRIORITY
            thread.isDaemon = true
            return thread
        }

    }

}