package com.example.aplikacijazaprognozuvremena

import java.util.Calendar

fun getBackgroundColor(): Int {
    val calendar = Calendar.getInstance()
    return when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 6..18 -> R.drawable.gradient_sunny_bg
        else -> R.drawable.gradient_cloudy_bg
    }

}