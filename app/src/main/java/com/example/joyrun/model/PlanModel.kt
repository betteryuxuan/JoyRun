package com.example.joyrun.model

import androidx.lifecycle.LiveData
import com.example.joyrun.DAO.RunningEventDao
import com.example.joyrun.bean.RunningEvent

class PlanModel(private val dao: RunningEventDao) {
    val allRunningEvents: LiveData<List<RunningEvent>> = dao.getAllAsLiveData()

    fun loadEventsByDate(startOfDay: Long, endOfDay: Long): LiveData<List<RunningEvent>> =
        dao.getEventsByDateAsLiveData(startOfDay, endOfDay)

    fun deleteEvent(event: RunningEvent) = dao.delete(event)
}
