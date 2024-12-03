package com.reza.mbahlaptop.customview.customButton

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.reza.mbahlaptop.R

class ButtonWithIcons @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val leadingIcon: ImageView
    private val trailingIcon: ImageView
    private val buttonText: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.button_with_icons, this, true)
        leadingIcon = findViewById(R.id.leading_icon)
        trailingIcon = findViewById(R.id.trailing_icon)
        buttonText = findViewById(R.id.button_text)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ButtonWithIcons, 0, 0)
            val text = typedArray.getString(R.styleable.ButtonWithIcons_text) ?: ""
            val leadingIconRes =
                typedArray.getResourceId(R.styleable.ButtonWithIcons_leadingIcon, 0)
            val trailingIconRes =
                typedArray.getResourceId(R.styleable.ButtonWithIcons_trailingIcon, 0)

            buttonText.text = text
            if (leadingIconRes != 0) leadingIcon.setImageResource(leadingIconRes)
            if (trailingIconRes != 0) trailingIcon.setImageResource(trailingIconRes)

            typedArray.recycle()
        }
    }
}
