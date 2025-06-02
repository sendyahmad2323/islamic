package com.example.islamic.ui.calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.islamic.R
import android.widget.Toast
import com.example.islamic.data.model.PrayerTime
import com.example.islamic.utils.HijriCalendarHelper
import com.example.islamic.ui.calendar.PrayerTimesAdapter

class HijriCalendarFragment : Fragment() {
    private lateinit var hijriCalendarHelper: HijriCalendarHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hijri_calendar, container, false)
        // ViewPager2 untuk kalender
        val viewPager = view.findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPagerCalendar)
        viewPager.adapter = CalendarPagerAdapter { selectedMillis ->
            // Tampilkan tanggal yang dipilih (bisa kamu ganti logikanya)
            Toast.makeText(requireContext(), "Tanggal dipilih: $selectedMillis", Toast.LENGTH_SHORT).show()
        }
        viewPager.setCurrentItem(CalendarPagerAdapter.MID_PAGE, false) // Tampilkan bulan sekarang


        hijriCalendarHelper = HijriCalendarHelper()

        // Set current hijri date
        view.findViewById<TextView>(R.id.tvHijriDate).text = hijriCalendarHelper.getCurrentHijriDate()
        view.findViewById<TextView>(R.id.tvGregorianDate).text = hijriCalendarHelper.getCurrentGregorianDate()

        // Setup prayer times recycler view
        val prayerTimes = listOf(
            PrayerTime("Amadan", "14:47 | 2023/4"),
            PrayerTime("Birnwalblu Sabakum Tidar", "Birnwalblu Sabakum Tidar"),
            PrayerTime("Mambacca Al Quran", "Shabat Dhuiho"),
            PrayerTime("Mambacca Didkit Patang", ""),
            PrayerTime("Batslavak", ""),
            PrayerTime("Shokat Tahayat", ""),
            PrayerTime("Birnwalblu sabakum tidar", ""),
            PrayerTime("Batslad", "")
        )
        view.findViewById<RecyclerView>(R.id.rvPrayerTimes).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = PrayerTimesAdapter(prayerTimes)
        }

        return view
    }
}