package com.intermediate.storyapp.costumview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

import com.intermediate.storyapp.R
import com.intermediate.storyapp.util.isValidEmail

class MyEditText : AppCompatEditText, View.OnTouchListener {

    private var isUsername: Boolean = false
    private var isEmail: Boolean = false
    private var isPassword: Boolean = false
    private lateinit var clearButtonIcon: Drawable
    private lateinit var emailIcon: Drawable
    private lateinit var passwordIcon: Drawable
    private lateinit var usernameIcon: Drawable
    private lateinit var enabledBackground: Drawable


    constructor(context :Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
    ) {
        init(attrs, defStyleAttr)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        setPadding(32, 32, 32, 32)
        background = enabledBackground
        gravity = Gravity.CENTER_VERTICAL
        compoundDrawablePadding = 16
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int = 0) {
        val attrs = context.obtainStyledAttributes(attrs, R.styleable.MyEditText, defStyleAttr, 0)

        isEmail = attrs.getBoolean(R.styleable.MyEditText_email, false)
        isPassword = attrs.getBoolean(R.styleable.MyEditText_password, false)
        isUsername = attrs.getBoolean(R.styleable.MyEditText_username, false)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.edit_text_bg) as Drawable
        clearButtonIcon = ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
        emailIcon = ContextCompat.getDrawable(context, R.drawable.ic_email) as Drawable
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_pass) as Drawable
        usernameIcon = ContextCompat.getDrawable(context, R.drawable.ic_person) as Drawable

        if (isEmail) {
            setButtonDrawables(startOfTheText = emailIcon)
        } else if (isPassword) {
            setButtonDrawables(startOfTheText = passwordIcon)
        } else if (isUsername) {
            setButtonDrawables(startOfTheText = usernameIcon)
        }

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p: CharSequence, start: Int, before: Int, count: Int) {
                val input = p.toString()
                val emailError = resources.getString(R.string.invalid_email)
                val passwordError = resources.getString(R.string.invalid_password)

                if (p.toString().isNotEmpty()) showClearButton() else hideClearButton()
                error =
                    if (isPassword && input.length < 8 && input.isNotEmpty()) {
                        passwordError
                    } else if (isEmail && !input.isValidEmail()) {
                        emailError
                    } else {
                        null
                    }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        attrs.recycle()
    }

    private fun showClearButton() {
        when {
            isEmail -> setButtonDrawables(
                startOfTheText = emailIcon,
                endOfTheText = clearButtonIcon
            )
            isPassword -> setButtonDrawables(
                startOfTheText = passwordIcon,
                endOfTheText = clearButtonIcon
            )
            isUsername -> setButtonDrawables(
                startOfTheText = usernameIcon,
                endOfTheText = clearButtonIcon
            )
            else -> setButtonDrawables(endOfTheText = clearButtonIcon)
        }
    }

    private fun hideClearButton() {
        when {
            isEmail -> setButtonDrawables(startOfTheText = emailIcon)
            isPassword -> setButtonDrawables(startOfTheText = passwordIcon)
            isUsername -> setButtonDrawables(startOfTheText = usernameIcon)
            else -> setButtonDrawables()
        }
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonIcon.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonIcon.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonIcon =
                            ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonIcon =
                            ContextCompat.getDrawable(context, R.drawable.ic_clear) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }
}