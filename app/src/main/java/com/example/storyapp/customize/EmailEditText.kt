package com.example.storyapp.customize

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R

class EmailEditText : AppCompatEditText, View.OnFocusChangeListener {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        onFocusChangeListener = this
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "email"
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus){
            if (!text.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()){
                error = context.getString(R.string.invalid_email)
            }
        }
    }
}