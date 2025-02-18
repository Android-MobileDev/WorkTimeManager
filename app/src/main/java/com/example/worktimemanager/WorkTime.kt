package com.example.worktimemanager

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "work_time_table")
data class WorkTime (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val startTime: String,
    val endTime: String,
) {
    fun getTotalHours(): String {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        try {
            val startDate = timeFormat.parse(startTime)
            val endDate = timeFormat.parse(endTime)

            if (startDate != null && endDate != null) {
                var difference = endDate.time - startDate.time

                if ( difference < 0) {
                    difference += 24 * 60 * 60 * 1000
                }

                val totalMinutes = (difference / (1000 * 60)).toInt()
                val hours = totalMinutes / 60
                val minutes = totalMinutes % 60
                return String.format("%02d:%02d", hours, minutes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "00:00"
    }
}