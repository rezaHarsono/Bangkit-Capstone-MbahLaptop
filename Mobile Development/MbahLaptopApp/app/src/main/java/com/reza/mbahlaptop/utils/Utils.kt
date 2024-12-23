package com.reza.mbahlaptop.utils

import android.icu.text.NumberFormat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.util.Locale
import java.util.concurrent.TimeUnit

fun formatToRelativeTime(dateString: String): String {
    return try {
        val parsedDate = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
        val now = ZonedDateTime.now()

        val duration = ChronoUnit.SECONDS.between(parsedDate, now)

        when {
            duration < 60 -> "$duration seconds ago"
            duration < 3600 -> "${TimeUnit.SECONDS.toMinutes(duration)} minutes ago"
            duration < 86400 -> "${TimeUnit.SECONDS.toHours(duration)} hours ago"
            else -> "${TimeUnit.SECONDS.toDays(duration)} days ago"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Unknown time"
    }
}

fun String.withCurrencyFormat(): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)

    return numberFormat.format(this.toDouble()).toString()
}

fun formatGBValue(value: Float): String {
    val roundValue = value.toInt()
    return "$roundValue GB"
}

fun ViewPager2.updateHeight() {
    val currentView = (getChildAt(0) as RecyclerView).layoutManager?.findViewByPosition(currentItem)
    currentView?.post {
        val params = layoutParams
        params.height = currentView.measuredHeight
        layoutParams = params
    }
}
