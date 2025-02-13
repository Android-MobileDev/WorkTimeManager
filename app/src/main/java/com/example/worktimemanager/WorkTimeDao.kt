package com.example.worktimemanager

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkTimeDao {
    @Insert
    suspend fun insertWorkTime(workTime: WorkTime)

    @Query("SELECT * FROM work_time_table WHERE date = :date")
    suspend fun getWorkTimeByDate(date: String): WorkTime?

    @Query("SELECT * FROM work_time_table")
    suspend fun getAllWorkTimes(): List<WorkTime>
}