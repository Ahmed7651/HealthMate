package com.k2703120.healthmate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.k2703120.healthmate.data.database.HealthDatabase
import com.k2703120.healthmate.data.model.HealthRecord
import com.k2703120.healthmate.data.repository.HealthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Manages health records and application state
class HealthViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository = HealthRepository(
        HealthDatabase.getDatabase(application).healthRecordDao()
    )

    val allRecords = repository.allRecords.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insertRecord(record: HealthRecord) {
        viewModelScope.launch {
            repository.insertRecord(record)
        }
    }

    fun updateRecord(record: HealthRecord) {
        viewModelScope.launch {
            repository.updateRecord(record)
        }
    }

    fun deleteRecord(record: HealthRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }

    private val _selectedDate = MutableStateFlow("")
    val selectedDate = _selectedDate.asStateFlow()

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun clearDateFilter() {
        _selectedDate.value = ""
    }

    val filteredRecords = combine(
        allRecords,
        selectedDate
    ) { records, date ->

        if (date.isEmpty()) {
            records
        } else {
            records.filter { it.date == date }
        }

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun getTodayDate(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())
    }
}