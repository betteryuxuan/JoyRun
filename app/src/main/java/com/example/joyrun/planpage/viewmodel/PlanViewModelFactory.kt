package com.example.joyrun.planpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.joyrun.db.RunningEventDao

class PlanViewModelFactory(private val dao: RunningEventDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlanViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}