package com.example.worktimemanager

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.worktimemanager.ui.theme.WorkTimeManagerTheme
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : ComponentActivity() {

    private lateinit var database: WorkTimeDatabase
    private lateinit var  workTimeDao: WorkTimeDao

    private var selectedDate: String = ""
    private var startTime: String = ""
    private var endTime: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        setContentView(R.layout.activity_main)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val selectedDateText = findViewById<TextView>(R.id.selectedDateText)

        val startTimeButton = findViewById<Button>(R.id.startTimeButton)
        val startTimeText = findViewById<TextView>(R.id.startTimeText)

        val endTimeButton = findViewById<Button>(R.id.endTimeButton)
        val endTimeText = findViewById<TextView>(R.id.endTimeText)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val viewSaved = findViewById<Button>(R.id.viewSavedButton)

        database = WorkTimeDatabase.getDatabase(this)
        workTimeDao = database.workTimeDao()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            selectedDateText.text = "Selected Date: $selectedDate"
        }

        startTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                startTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                startTimeText.text = "Start Time: $startTime"
            }, hour, minute, true)

            timePicker.show()
        }

        endTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                endTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                endTimeText.text = "End Time: $endTime"
            }, hour, minute, true)

            timePicker.show()
        }

        saveButton.setOnClickListener {
            if (selectedDate.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty()) {
                val workTime = WorkTime(date = selectedDate, startTime = startTime, endTime = endTime)

                lifecycleScope.launch {
                    workTimeDao.insertWorkTime(workTime)

                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Work time saved!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewSaved.setOnClickListener {
            val intent = Intent(this, WorkTimeList::class.java)
            startActivity(intent)
        }
    }
}