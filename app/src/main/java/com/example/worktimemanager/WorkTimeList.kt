package com.example.worktimemanager

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worktimemanager.ui.theme.WorkTimeManagerTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class WorkTimeList : ComponentActivity() {

    private lateinit var database: WorkTimeDatabase
    private lateinit var workTimeDao: WorkTimeDao
    private lateinit var workTimeAdapter: WorkTimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        setContentView(R.layout.activity_work_time_list)

        val recyclerView = findViewById<RecyclerView>(R.id.workTimeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val totalWeeklyTextView = findViewById<TextView>(R.id.totalWeeklyTextView)
        val totalMonthlyTextView = findViewById<TextView>(R.id.totalMonthlyTextView)

        database = WorkTimeDatabase.getDatabase(this)
        workTimeDao = database.workTimeDao()

        lifecycleScope.launch {
            val workTimes = workTimeDao.getAllWorkTimes()

            val sortedWorkTimes = workTimes.sortedByDescending { workTime ->
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(workTime.date)
            }

            workTimeAdapter = WorkTimeAdapter(sortedWorkTimes)

            val lastWeekWorkTimes = getWorkTimesForLastWeek()
            val totalWeekly = calculateTotalHours(lastWeekWorkTimes)

            val lastMonthWorkTimes = getWorkTimesForLastMonth()
            val totalMonthly = calculateTotalHours(lastMonthWorkTimes)

            runOnUiThread {
                recyclerView.adapter = workTimeAdapter
                totalWeeklyTextView.text = "Total Last Week: $totalWeekly hrs"
                totalMonthlyTextView.text = "Total Last Month: $totalMonthly hrs"
            }
        }


        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun calculateTotalHours(workTimes: List<WorkTime>): String {
        var totalMinutes = 0
        for (workTime in workTimes) {
            val totalTimeString = workTime.getTotalHours().trim()

            println("Time worked for ${workTime.date}: $totalTimeString")

            val timeParts = totalTimeString.split(":")

            if (timeParts.size == 2) {
                val hours = timeParts[0].toInt()
                val minutes = timeParts[1].toInt()
                totalMinutes += (hours * 60) + minutes
            }
        }
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return String.format("%02d:%02d", hours, minutes)
    }

    private suspend fun getWorkTimesForLastWeek(): List<WorkTime> {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val startOfLastWeek = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, 6)
        val endOfLastWeek = calendar.time

        println("Fetching work times for last week from ${startOfLastWeek} to ${endOfLastWeek}")

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return workTimeDao.getAllWorkTimes().filter { workTime ->
            val workDate = dateFormat.parse(workTime.date) ?: return@filter false
            workDate in startOfLastWeek..endOfLastWeek
        }
    }

    private suspend fun getWorkTimesForLastMonth(): List<WorkTime> {
        val calendar = Calendar.getInstance()

        // Move to the first day of the previous month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, -1)
        val startOfMonth = calendar.time

        // Move to the last day of the previous month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val endOfMonth = calendar.time

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        println(
            "Fetching work times for last month from ${dateFormat.format(startOfMonth)} to ${
                dateFormat.format(
                    endOfMonth
                )
            }"
        )

        return workTimeDao.getAllWorkTimes().filter { workTime ->
            val workDate = dateFormat.parse(workTime.date) ?: return@filter false
            workDate in startOfMonth..endOfMonth
        }
    }
}