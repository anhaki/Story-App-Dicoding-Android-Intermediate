package com.haki.storyapp.customView

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.haki.storyapp.R

class MyButton : MaterialButton {
    private var enableColor: Int = 0
    private var disableColor: Int = 0
    private var txtColor: Int = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        enableColor = ContextCompat.getColor(context, R.color.mainCol)
        disableColor = ContextCompat.getColor(context, R.color.mainCol2)
        txtColor = ContextCompat.getColor(context, R.color.white)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled)
        )

        val colors = intArrayOf(enableColor, disableColor)

        val colorStateList = ColorStateList(states, colors)
        backgroundTintList = colorStateList

        setTextColor(txtColor)
    }

}