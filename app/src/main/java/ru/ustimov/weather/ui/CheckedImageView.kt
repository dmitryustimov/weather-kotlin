package ru.ustimov.weather.ui

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import ru.ustimov.weather.R

class CheckedImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr), Checkable {

    private companion object {

        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)

    }

    interface OnCheckedChangeListener {

        fun onCheckedChanged(v: CheckedImageView, isChecked: Boolean)

    }

    private var checked = false
    private var broadcasting = false
    private var onCheckedChangeListener: OnCheckedChangeListener? = null

    init {
        isClickable = true

        val a = context.obtainStyledAttributes(attrs, R.styleable.CheckedImageView, defStyleAttr, 0)
        try {
            val checked = a.getBoolean(R.styleable.CheckedImageView_android_checked, false)
            this.checked = checked
        } finally {
            a.recycle()
        }
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        onCheckedChangeListener = listener
    }

    override fun setChecked(checked: Boolean) {
        if (this.checked != checked) {
            this.checked = checked
            refreshDrawableState()

            // Avoid infinite recursions if setChecked() is called from a listener
            if (!broadcasting) {
                broadcasting = true
                onCheckedChangeListener?.onCheckedChanged(this, this.checked)
                broadcasting = false
            }
        }
    }

    override fun isChecked() = this.checked

    override fun toggle() {
        isChecked = !this.checked
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    override fun performClick(): Boolean {
        /* When clicked, toggle the state */
        toggle()
        return super.performClick()
    }

    private class SavedState : View.BaseSavedState {

        var checked: Boolean

        internal constructor(superState: Parcelable, checked: Boolean) : super(superState) {
            this.checked = checked
        }

        private constructor(src: Parcel) : super(src) {
            checked = src.readInt() == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeValue(if (checked) 1 else 0)
        }

        override fun toString(): String {
            return ("CheckedImageView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}")
        }

        companion object {

            @JvmStatic
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(src: Parcel) = SavedState(src)

                override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)

            }
        }

    }

    public override fun onSaveInstanceState(): Parcelable {
        // Force our ancestor class to save its state
        val superState = super.onSaveInstanceState()
        return SavedState(superState, isChecked)
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.getSuperState())
        isChecked = ss.checked
        requestLayout()
    }

}