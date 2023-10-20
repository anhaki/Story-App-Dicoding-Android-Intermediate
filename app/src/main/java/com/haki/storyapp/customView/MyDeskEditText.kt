package com.haki.storyapp.customView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import com.haki.storyapp.R

class MyDeskEditText : AppCompatEditText, View.OnTouchListener {

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
        doOnTextChanged { text, _, _, _ ->
            if (text.toString().isEmpty()) {
                error = context.getString(R.string.deskerror)
            }
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

}