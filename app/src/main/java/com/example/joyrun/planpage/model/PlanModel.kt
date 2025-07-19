package com.example.joyrun.planpage.model

import androidx.lifecycle.LiveData
import com.example.joyrun.bean.RunningEvent
import com.example.joyrun.db.RunningEventDao

class PlanModel(private val dao: RunningEventDao) {
    val allRunningEvents: LiveData<List<RunningEvent>> = dao.getAllAsLiveData()

    fun loadEventsByDate(startOfDay: Long, endOfDay: Long): LiveData<List<RunningEvent>> =
        dao.getEventsByDateAsLiveData(startOfDay, endOfDay)

    fun deleteEvent(event: RunningEvent) = dao.delete(event)
}