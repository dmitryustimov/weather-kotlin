package ru.ustimov.weather.content.impl.glide

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.design.widget.TabLayout
import android.view.View
import com.bumptech.glide.request.target.BaseTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.transition.Transition

class TabIconTarget(
        private val tab: TabLayout.Tab,
        private val width: Int,
        private val height: Int
) : BaseTarget<Drawable>(), Transition.ViewAdapter {

    private var animatable: Animatable? = null

    override fun getView(): View? = tab.customView

    override fun getCurrentDrawable(): Drawable? = tab.icon

    override fun setDrawable(drawable: Drawable?) {
        tab.icon = drawable
    }

    override fun getSize(cb: SizeReadyCallback?) {
        cb?.onSizeReady(width, height)
    }

    override fun removeCallback(cb: SizeReadyCallback?) {
    }

    override fun onLoadStarted(placeholder: Drawable?) {
        super.onLoadStarted(placeholder)
        setResourceInternal(null)
        setDrawable(placeholder)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        setResourceInternal(null)
        setDrawable(errorDrawable)
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        super.onLoadCleared(placeholder)
        setResourceInternal(null)
        setDrawable(placeholder)
    }

    override fun onResourceReady(resource: Drawable?, transition: Transition<in Drawable>?) {
        if (transition == null || !transition.transition(resource, this)) {
            setResourceInternal(resource)
        } else {
            maybeUpdateAnimatable(resource)
        }
    }

    override fun onStart() {
        animatable?.start()
    }

    override fun onStop() {
        animatable?.stop()
    }

    private fun setResourceInternal(resource: Drawable?) {
        maybeUpdateAnimatable(resource)
        setDrawable(resource)
    }

    private fun maybeUpdateAnimatable(resource: Drawable?) {
        if (resource is Animatable) {
            animatable = resource
            resource.start()
        } else {
            animatable = null
        }
    }

}