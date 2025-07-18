package com.example.joyrun.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.joyrun.bean.RunningEvent

@Dao
interface RunningEventDao {
    @Query("SELECT * FROM RunningEvent")
    fun getAll(): List<RunningEvent>

    @Query("SELECT * FROM RunningEvent ORDER BY startTime DESC")
    fun getAllAsLiveData(): LiveData<List<RunningEvent>>

    @Query("SELECT * FROM RunningEvent WHERE startTime >= :startOfDay  AND startTime <= :endOfDay")
    fun getEventsByDateAsLiveData(startOfDay: Long, endOfDay: Long): LiveData<List<RunningEvent>>

    @Insert
    fun insert(runningEvent: RunningEvent)

    @Delete
    fun delete(runningEvent: RunningEvent)

}