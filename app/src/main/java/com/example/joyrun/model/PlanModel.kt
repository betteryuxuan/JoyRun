package com.example.joyrun.model

import androidx.lifecycle.LiveData
import com.example.joyrun.DAO.RunningEventDao
import com.example.joyrun.bean.RunningEvent

class PlanModel (private val dao: RunningEventDao){
    val allRunningEvents: LiveData<List<RunningEvent>> = dao.getAllAsLiveData()

}