package ru.ustimov.weather.rx

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.support.annotation.CheckResult
import io.reactivex.Single

class RxIntent private constructor() {

    companion object {

        @CheckResult
        fun resolveActivity(context: Context, intent: Intent): Single<Intent> {
            return Single.create({ emitter ->
                if (resolveActivityImpl(context, intent)) {
                    if (!emitter.isDisposed) {
                        emitter.onSuccess(intent)
                    }
                } else if (!emitter.isDisposed) {
                    emitter.onError(ActivityNotFoundException("No app found to handle $intent"))
                }
            })
        }

        @CheckResult
        private fun resolveActivityImpl(context: Context, intent: Intent): Boolean {
            val rawIntent = getRawIntent(intent)
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PackageManager.MATCH_ALL else 0
            val resolvedActivities = queryActivities(context, rawIntent, flags)
            return !resolvedActivities.isEmpty()
        }

        @CheckResult
        private fun getRawIntent(intent: Intent) =
                if (Intent.ACTION_CHOOSER == intent.action) {
                    intent.getParcelableExtra(Intent.EXTRA_INTENT)
                } else {
                    intent
                }

        @CheckResult
        private fun queryActivities(context: Context, intent: Intent, flags: Int = 0): List<ResolveInfo> {
            val packageManager = context.packageManager
            val activities = packageManager.queryIntentActivities(intent, flags)
            return activities ?: emptyList()
        }

    }

}