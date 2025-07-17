package com.example.joyrun.model

import com.example.joyrun.DAO.RunningEventDao
import com.example.joyrun.bean.RunningEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountDownModel(private val dao: RunningEventDao) {
    suspend fun insert(runningEvent: RunningEvent) {
        dao.insert(runningEvent)
    }

    suspend fun getAllEvents(): List<RunningEvent> {
        return withContext(Dispatchers.IO) {
            dao.getAll()
        }
    }

    suspend fun deleteEvent(event: RunningEvent) {
        withContext(Dispatchers.IO) {
            dao.delete(event)
        }
    }
}