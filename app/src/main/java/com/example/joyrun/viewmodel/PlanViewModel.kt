package com.example.joyrun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.joyrun.DAO.RunningEventDao
import com.example.joyrun.bean.RunningEvent
import com.example.joyrun.model.PlanModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlanViewModel(dao: RunningEventDao) : ViewModel() {
    private val model = PlanModel(dao)
    private var currentStartTime: Long? = null
    private var currentEndTime: Long? = null

    private val _allRunningEvents = MutableLiveData<List<RunningEvent>>()
    val allRunningEvents: LiveData<List<RunningEvent>> get() = _allRunningEvents

    private val _totalTime = MutableLiveData<Long>()
    val totalTime: LiveData<Long> get() = _totalTime

    fun loadEventsByDate(startOfDay: Long, endOfDay: Long) {
        currentStartTime = startOfDay
        currentEndTime = endOfDay
        val result = model.loadEventsByDate(startOfDay, endOfDay)
        result.observeForever { events ->
            _allRunningEvents.postValue(events)
            _totalTime.postValue(events.sumOf { it.duration })
        }

    }


    fun deleteEvent(event: RunningEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            model.deleteEvent(event)
            if (currentStartTime != null && currentEndTime != null) {
                loadEventsByDate(currentStartTime!!, currentEndTime!!)
            }

        }
    }
}
