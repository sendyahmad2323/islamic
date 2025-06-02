package com.example.islamic.ui.prayer

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import com.example.islamic.AlarmReceiver
import com.example.islamic.MyLocationHelper
import com.example.islamic.R
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class JadwalSholatFragment : Fragment() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001

        // Request codes unik untuk setiap jadwal sholat
        private const val REQ_SUBUH = 100
        private const val REQ_DHUZUR = 101
        private const val REQ_ASHAR = 102
        private const val REQ_MAGHRIB = 103
        private const val REQ_ISYA = 104
    }

    private lateinit var locationHelper: MyLocationHelper

    // Simpan waktu prayerTimes agar bisa digunakan saat menjadwalkan alarm
    private var currentPrayerTimes: PrayerTimes? = null

    // Icon‐icon notifikasi
    private lateinit var ivSubuhNotify: ImageView
    private lateinit var ivDhuzurNotify: ImageView
    private lateinit var ivAsharNotify: ImageView
    private lateinit var ivMaghribNotify: ImageView
    private lateinit var ivIsyaNotify: ImageView

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationHelper.getLastLocation()
            } else {
                Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_jadwal_sholat, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi MyLocationHelper
        locationHelper = MyLocationHelper(requireContext(), object : MyLocationHelper.LocationCallback {
            override fun onLocationReceived(location: Location) {
                val lat = location.latitude
                val lon = location.longitude

                // Hitung jadwal sholat menggunakan Adhan library
                val coordinates = Coordinates(lat, lon)
                val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
                val prayerTimes = PrayerTimes(
                    coordinates,
                    DateComponents.from(Calendar.getInstance().time),
                    params
                )

                currentPrayerTimes = prayerTimes

                // Update UI
                updatePrayerTimesUI(view, prayerTimes)
                updateCurrentDate(view)

                // Inisialisasi ikon notifikasi (mati atau aktif)
                ivSubuhNotify = view.findViewById(R.id.ivSubuhNotify)
                ivDhuzurNotify = view.findViewById(R.id.ivDhuzurNotify)
                ivAsharNotify = view.findViewById(R.id.ivAsharNotify)
                ivMaghribNotify = view.findViewById(R.id.ivMaghribNotify)
                ivIsyaNotify = view.findViewById(R.id.ivIsyaNotify)

                setupNotificationListeners()

                // Ambil nama lokasi (opsional)
                locationHelper.getLocationName(lat, lon)
            }

            override fun onLocationError(errorMessage: String) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onLocationNameReceived(locationName: String) {
                Toast.makeText(requireContext(), "Kota: $locationName", Toast.LENGTH_SHORT).show()
            }
        })

        // Minta izin lokasi jika belum ada
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationHelper.getLastLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun updatePrayerTimesUI(view: View, prayerTimes: PrayerTimes) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        view.findViewById<TextView>(R.id.tvSubuhTime).text = timeFormat.format(prayerTimes.fajr)
        view.findViewById<TextView>(R.id.tvDhuzurTime).text = timeFormat.format(prayerTimes.dhuhr)
        view.findViewById<TextView>(R.id.tvAsharTime).text = timeFormat.format(prayerTimes.asr)
        view.findViewById<TextView>(R.id.tvMaghribTime).text = timeFormat.format(prayerTimes.maghrib)
        view.findViewById<TextView>(R.id.tvIsyaTime).text = timeFormat.format(prayerTimes.isha)
    }

    private fun updateCurrentDate(view: View) {
        // Tanggal Gregorian
        val calendar = Calendar.getInstance()
        val dayOfWeek = android.text.format.DateFormat.format("EEEE", calendar)
        val fullDate = android.text.format.DateFormat.format("MMMM dd, yyyy", calendar)

        // Tanggal Hijri menggunakan Umm al-Qura
        val hijri = UmmalquraCalendar()
        val hijriDay = hijri.get(Calendar.DAY_OF_MONTH)
        val hijriMonthIndex = hijri.get(Calendar.MONTH)
        val hijriYear = hijri.get(Calendar.YEAR)
        val hijriMonthNames = arrayOf(
            "Muharram", "Safar", "Rabiʿ al‑awwal", "Rabiʿ al‑thani",
            "Jumada al‑awwal", "Jumada al‑thani", "Rajab", "Shaʿban",
            "Ramadan", "Shawwal", "Dhu al‑Qiʿdah", "Dhu al‑Ḥijjah"
        )
        val hijriMonthName = hijriMonthNames[hijriMonthIndex]
        val hijriDate = "$hijriDay $hijriMonthName, $hijriYear"

        view.findViewById<TextView>(R.id.tvDay).text = "Today / $dayOfWeek"
        view.findViewById<TextView>(R.id.tvDate).text = "$fullDate / $hijriDate"
    }

    private fun setupNotificationListeners() {
        // Subuh
        ivSubuhNotify.setOnClickListener {
            currentPrayerTimes?.let { pt ->
                toggleAlarm(
                    timeInMillis = pt.fajr.time,
                    requestCode = REQ_SUBUH,
                    pesan = "Waktu Subuh telah tiba!",
                    iconView = ivSubuhNotify
                )
            }
        }
        // Dhuzur
        ivDhuzurNotify.setOnClickListener {
            currentPrayerTimes?.let { pt ->
                toggleAlarm(
                    timeInMillis = pt.dhuhr.time,
                    requestCode = REQ_DHUZUR,
                    pesan = "Waktu Dhuzur telah tiba!",
                    iconView = ivDhuzurNotify
                )
            }
        }
        // Ashar
        ivAsharNotify.setOnClickListener {
            currentPrayerTimes?.let { pt ->
                toggleAlarm(
                    timeInMillis = pt.asr.time,
                    requestCode = REQ_ASHAR,
                    pesan = "Waktu Ashar telah tiba!",
                    iconView = ivAsharNotify
                )
            }
        }
        // Maghrib
        ivMaghribNotify.setOnClickListener {
            currentPrayerTimes?.let { pt ->
                toggleAlarm(
                    timeInMillis = pt.maghrib.time,
                    requestCode = REQ_MAGHRIB,
                    pesan = "Waktu Maghrib telah tiba!",
                    iconView = ivMaghribNotify
                )
            }
        }
        // Isya
        ivIsyaNotify.setOnClickListener {
            currentPrayerTimes?.let { pt ->
                toggleAlarm(
                    timeInMillis = pt.isha.time,
                    requestCode = REQ_ISYA,
                    pesan = "Waktu Isya telah tiba!",
                    iconView = ivIsyaNotify
                )
            }
        }
    }

    /**
     * Jika belum ada alarm dengan requestCode, jadwalkan. Kalau sudah ada, batalkan.
     * Juga mengganti ikon antara ic_notifications_off <-> ic_notifications_on.
     */
    private fun toggleAlarm(
        timeInMillis: Long,
        requestCode: Int,
        pesan: String,
        iconView: ImageView
    ) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("pesan", pesan)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_MUTABLE else 0
        )

        // Cek apakah alarm sudah dijadwalkan (PendingIntent.getBroadcast non-null berarti sudah ada)
        val existing = PendingIntent.getBroadcast(
            requireContext(),
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_MUTABLE else 0
        )

        if (existing != null) {
            // Kalau sudah ada: batalkan alarm
            alarmManager.cancel(pendingIntent)
            iconView.setImageResource(R.drawable.ic_notifications_off)
            Toast.makeText(requireContext(), "Notifikasi dibatalkan", Toast.LENGTH_SHORT).show()
        } else {
            // Jadwalkan alarm pada waktu timeInMillis
            if (timeInMillis <= System.currentTimeMillis()) {
                // Jika waktu sudah lewat (misalnya jam sudah lewat), beri tahu
                Toast.makeText(requireContext(), "Waktu sudah lewat, tidak bisa dijadwalkan", Toast.LENGTH_SHORT).show()
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
            iconView.setImageResource(R.drawable.ic_notifications_on)
            Toast.makeText(requireContext(), "Notifikasi dihidupkan", Toast.LENGTH_SHORT).show()
        }
    }
}
