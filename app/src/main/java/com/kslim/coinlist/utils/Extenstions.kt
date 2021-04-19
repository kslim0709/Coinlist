package com.kslim.coinlist.utils

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.kslim.coinlist.R
import java.text.DecimalFormat

fun EditText.onSearchTextChanged(completion: (Editable) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            completion.invoke(s as Editable)
        }

        override fun afterTextChanged(s: Editable?) {
        }

    })
}

fun Double.numberFormat(fracDigits: Int): String {
    val df = DecimalFormat()
    df.maximumFractionDigits = 2
    df.minimumFractionDigits = fracDigits
    return df.format(this)
}

fun getColor(change: String): Int {
    return when (change) {
        Constants.EVEN -> {
            R.color.black
        }
        Constants.FALL -> {
            R.color.color_00000FF
        }
        Constants.RISE -> {
            R.color.color_FF0000
        }
        else -> R.color.black
    }
}
