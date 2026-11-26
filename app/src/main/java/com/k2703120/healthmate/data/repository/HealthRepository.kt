package com.k2703120.healthmate.data.repository

import com.k2703120.healthmate.data.database.HealthRecordDao
import com.k2703120.healthmate.data.model.HealthRecord

// Manages access to health records
class HealthRepository(private val dao: HealthRecordDao) {

    val allRecords = dao.getAllRecords()

    suspend fun insertRecord(record: HealthRecord) {
        dao.insertRecord(record)
    }

    suspend fun updateRecord(record: HealthRecord) {
        dao.updateRecord(record)
    }

    suspend fun deleteRecord(record: HealthRecord) {
        dao.deleteRecord(record)
    }

    fun getRecordsByDate(date: String) =
        dao.getRecordsByDate(date)

    suspend fun getRecordById(id: Int) =
        dao.getRecordById(id)
}