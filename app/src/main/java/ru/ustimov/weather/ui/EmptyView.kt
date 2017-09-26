package ru.ustimov.weather.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import ru.ustimov.weather.R

class EmptyView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr) {

    private val textView: TextView
    private val actionButton: Button

    var onActionButtonClickListener: () -> Unit = {}

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER

        val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.empty_view_margin_horizontal)
        val paddingVertical = resources.getDimensionPixelSize(R.dimen.empty_view_margin_vertical)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

        inflate(context, R.layout.view_empty, this)
        textView = findViewById(R.id.text)
        actionButton = findViewById(R.id.action)

        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.EmptyView, defStyleAttr, 0)
        try {
            val text = a.getText(R.styleable.EmptyView_android_text)
            val drawableRes = a.getResourceId(R.styleable.EmptyView_android_drawable, 0)
            textView.text = text
            textView.setCompoundDrawablesWithIntrinsicBounds(0, drawableRes, 0, 0)
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