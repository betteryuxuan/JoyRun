package com.example.joyrun.runpage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.joyrun.db.RunningEventDao

class RunOutdoorViewModelFactory(private val dao: RunningEventDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunOutdoorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RunOutdoorViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
