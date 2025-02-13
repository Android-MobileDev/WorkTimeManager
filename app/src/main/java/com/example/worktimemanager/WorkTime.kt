package com.example.worktimemanager

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_time_table")
data class WorkTime (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val startTime: String,
    val endTime: String
)