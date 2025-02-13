package com.example.worktimemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class WorkTimeList : ComponentActivity() {

    private lateinit var database: WorkTimeDatabase
    private lateinit var workTimeDao: WorkTimeDao
    private lateinit var workTimeAdapter: WorkTimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_time_list)

        val recyclerView = findViewById<RecyclerView>(R.id.workTimeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        database = WorkTimeDatabase.getDatabase(this)
        workTimeDao = database.workTimeDao()

        lifecycleScope.launch {
            val workTimes = workTimeDao.getAllWorkTimes()
            workTimeAdapter = WorkTimeAdapter(workTimes)
            runOnUiThread {
                recyclerView.adapter = workTimeAdapter
            }
        }
    }
}