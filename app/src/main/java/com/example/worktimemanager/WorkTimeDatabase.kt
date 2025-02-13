package com.example.worktimemanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorkTime::class], version = 1)
abstract class WorkTimeDatabase : RoomDatabase() {

    abstract fun workTimeDao(): WorkTimeDao

    companion object {
        @Volatile
        private var INSTANCE: WorkTimeDatabase? = null

        fun getDatabase(context: Context): WorkTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkTimeDatabase::class.java,
                    "work_time_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}