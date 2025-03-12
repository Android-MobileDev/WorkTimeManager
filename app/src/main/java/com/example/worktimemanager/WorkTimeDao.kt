package com.example.worktimemanager

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WorkTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkTime(workTime: WorkTime)

    @Query("SELECT * FROM work_time_table ORDER BY date ASC")
    suspend fun getAllWorkTimes(): List<WorkTime>

    @Query("SELECT * FROM work_time_table WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getWorkTimesInRange(startDate: String, endDate: String): List<WorkTime>
}