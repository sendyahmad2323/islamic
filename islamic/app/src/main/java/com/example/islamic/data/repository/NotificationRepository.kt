package com.example.islamic.data.repository

import com.example.islamic.data.model.PrayerNotification

class NotificationRepository {

    // Contoh data dummy
    private val notifications = mutableListOf(
        PrayerNotification(
            id = "1",
            title = "Sholat Subuh",
            message = "Waktu Sholat Subuh telah tiba",
            timeAgo = "10m",
            isRead = false,
            prayerType = "SUBUH",
            prayerName = "Subuh",
            time = "04:30"
        ),
        PrayerNotification(
            id = "2",
            title = "Sholat Dzuhur",
            message = "Waktu Sholat Dzuhur telah tiba",
            timeAgo = "1h",
            isRead = false,
            prayerType = "DZUHUR",
            prayerName = "Dzuhur",
            time = "12:00"
        )
        // Tambah data lain jika perlu
    )


    suspend fun getNotifications(): List<PrayerNotification> {
        return notifications.toList()  // return copy
    }

    suspend fun markAsRead(notificationId: String) {
        // Contoh dummy: tidak ada status, jadi skip
        // Bisa implement update status jika ada atribut read/unread
    }

    suspend fun deleteNotification(notificationId: String) {
        notifications.removeAll { it.id == notificationId }
    }
}
