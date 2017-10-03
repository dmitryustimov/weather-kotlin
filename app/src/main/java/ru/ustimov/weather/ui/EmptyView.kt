package ru.ustimov.weather.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.content.res.AppCompatResources
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.view_empty.view.*
import ru.ustimov.weather.R

@ContainerOptions(CacheImplementation.SPARSE_ARRAY)
class EmptyView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private companion object {
        private const val INDEX_DRAWABLE_TOP = 1
    }

    var onActionButtonClickListener: () -> Unit = {}

    var text: CharSequence?
        get() = textView.text
        set(value) {
            textView.text = value
            textView.visibility = if (value.isNullOrEmpty() && textView.compoundDrawables[INDEX_DRAWABLE_TOP] == null) GONE else VISIBLE
        }

    var drawable: Drawable?
        get() = textView.compoundDrawables[INDEX_DRAWABLE_TOP]
        set(value) {
            textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            textView.visibility = if (text.isNullOrEmpty() && drawable == null) GONE else VISIBLE
        }

    var drawableRes: Int
        get() = 0
        set(value) {
            drawable = AppCompatResources.getDrawable(context, drawableRes)
        }

    var action: CharSequence?
        get() = actionButton.text
        set(value) {
            actionButton.text = value
            actionButton.visibility = if (value.isNullOrEmpty()) GONE else VISIBLE
        }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER

        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.empty_view_margin_horizontal)
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.empty_view_margin_vertical)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

        inflate(context, R.layout.view_empty, this)

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.EmptyView, defStyleAttr, 0)
        try {
            val text = a.getText(R.styleable.EmptyView_android_text)
            this.text = text

            if (a.hasValue(R.styleable.EmptyView_android_drawable)) {
                drawableRes = a.getResourceId(R.styleable.EmptyView_android_drawable, 0)
            }

            val action = a.getText(R.styleable.EmptyView_android_action)
            this.action = action
            actionButton.setOnClickListener({ onActionButtonClickListener() })
        } finally {
            a.recycle()
        }
    }

    data class Info(
            private val text: CharSequence? = null,
            private val drawable: Drawable? = null,
            private val action: CharSequence? = null,
            private val listener: () -> Unit = {}
    ) {

        fun apply(emptyView: EmptyView) {
            emptyView.action = action
            emptyView.drawable = drawable
            emptyView.action = action
            emptyView.onActionButtonClickListener = listener
        }

    }

}