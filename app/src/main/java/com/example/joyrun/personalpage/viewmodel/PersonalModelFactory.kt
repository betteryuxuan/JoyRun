package com.example.joyrun.personalpage.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.joyrun.db.RunningEventDao

class PersonalModelFactory(private val dao: RunningEventDao, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PersonalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PersonalViewModel(dao,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}