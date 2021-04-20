package com.kslim.coinlist.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DateAxisValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        val sdf = SimpleDateFormat("yyyy.MM.dd",Locale.getDefault())

        return sdf.format(Date(value.toLong()))
    }

}