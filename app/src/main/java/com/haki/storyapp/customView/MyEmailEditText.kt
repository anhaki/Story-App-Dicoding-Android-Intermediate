package com.haki.storyapp.customView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.haki.storyapp.R

class MyEmailEditText : AppCompatEditText, View.OnTouchListener {

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
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isValidEmail(s.toString())) {
                    isValid = false
                    error = context.getString(R.string.email_error)
                } else {
                    isValid = true
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                //Do nothing
            }
        })

    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

    private fun isValidEmail(email: String): Boolean {

        val emailRegex =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|org|net|edu|gov|mil|int|aero|coop|museum|name|biz|info|pro|asia|mobi|tel|jobs|travel|[a-zA-Z]{2})$"

        return email.matches(emailRegex.toRegex())
    }

    companion object {
        var isValid: Boolean = false
    }
}