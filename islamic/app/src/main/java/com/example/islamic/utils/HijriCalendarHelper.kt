package com.example.islamic.utils

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.text.SimpleDateFormat
import java.util.*

class HijriCalendarHelper {
    // Mendapatkan tanggal Hijriyah saat ini
    fun getCurrentHijriDate(): String {
       val cal = UmmalquraCalendar()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        val monthName = getHijriMonthName(month)

        return "$day $monthName $year"
    }

    // Mendapatkan nama bulan Hijriyah
    private fun getHijriMonthName(month: Int): String {
        val hijriMonths = arrayOf(
            "Muharram", "Safar", "Rabi'ul Awwal", "Rabi'ul Akhir",
            "Jumada'ul Awwal", "Jumada'ul Akhir", "Rajab", "Sha'ban",
            "Ramadan", "Shawwal", "Dhul-Qi'dah", "Dhul-Hijjah"
        )
        return hijriMonths[month]
    }

    // Mendapatkan tanggal Gregorian saat ini
    fun getCurrentGregorianDate(): String {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
