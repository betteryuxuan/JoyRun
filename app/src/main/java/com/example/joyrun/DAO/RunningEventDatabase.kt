package com.example.joyrun.DAO

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.joyrun.bean.RunningEvent
import com.example.joyrun.utils.LatLngListConverter

@Database(entities = [RunningEvent::class], version = 1, exportSchema = false)
@TypeConverters(LatLngListConverter::class)
abstract class RunningEventDatabase : RoomDatabase() {
    abstract fun runningEventDao(): RunningEventDao

    companion object {
        @Volatile
        private var INSTANCE: RunningEventDatabase? = null

        fun getInstance(context: Context): RunningEventDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RunningEventDatabase::class.java,
                    "running_event_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
