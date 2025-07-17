package com.example.joyrun.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.joyrun.DAO.RunningEventDao

class CountDownViewModelFactory(private val dao: RunningEventDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountDownViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountDownViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
