package com.example.joyrun.viewmodel

import androidx.lifecycle.ViewModel
import com.example.joyrun.DAO.RunningEventDao
import com.example.joyrun.model.PlanModel

class PlanViewModel(dao: RunningEventDao) : ViewModel() {
    private val model = PlanModel(dao)
    val allRunningEvents = model.allRunningEvents
}