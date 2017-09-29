package ru.ustimov.weather.ui

import android.content.Context
import android.support.v7.widget.AppCompatDrawableManager
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

    var onActionButtonClickListener: () -> Unit = {}

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
            textView.text = text

            var drawableRes = 0
            if (a.hasValue(R.styleable.EmptyView_android_drawable)) {
                drawableRes = a.getResourceId(R.styleable.EmptyView_android_drawable, 0)
                val drawable = AppCompatDrawableManager.get().getDrawable(context, drawableRes)
                textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            }

            textView.visibility = if (text.isNullOrEmpty() && drawableRes == 0) GONE else VISIBLE

            val action = a.getText(R.styleable.EmptyView_android_action)
            actionButton.text = action
            actionButton.setOnClickListener({ onActionButtonClickListener() })
            actionButton.visibility = if (action.isNullOrEmpty()) GONE else VISIBLE
        } finally {
            a.recycle()
        }
    }

}