package com.example.joyrun.personalpage.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.joyrun.db.RunningEventDao
import com.example.joyrun.personalpage.model.PersonalRepository

class PersonalViewModel(private val dao: RunningEventDao, private val context: Context) :
    ViewModel() {
    private val repository: PersonalRepository = PersonalRepository(dao, context)

    val totalDistance: LiveData<Float> = repository.getTotalDistance()
    val yearDistance: LiveData<Float> = repository.getYearDistance()
    val monthDistance: LiveData<Float> = repository.getMonthDistance()
    val todayDistance: LiveData<Float> = repository.getTodayDistance()

    private val _targetMonthDistance = MutableLiveData<Int>()
    val targetMonthDistance: LiveData<Int> get() = _targetMonthDistance
    private val _targetYearDistance = MutableLiveData<Int>()
    val targetYearDistance: LiveData<Int> get() = _targetYearDistance
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName
    init {
        _targetMonthDistance.value = repository.getTargetMonthDistance()
        _targetYearDistance.value = repository.getTargetYearDistance()
        _userName.value = repository.getUserName()
    }

    fun saveTargetMonthDistance(target: Int) {
        repository.saveTargetMonthDistance(target)
        _targetMonthDistance.value = target
    }

    fun saveTargetYearDistance(target: Int) {
        repository.saveTargetYearDistance(target)
        _targetYearDistance.value = target
    }

    fun saveUserName(name: String) {
        repository.saveUserName(name)
        _userName.value = name
    }
}
