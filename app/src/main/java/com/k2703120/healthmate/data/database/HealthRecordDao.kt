package com.k2703120.healthmate.data.database

import androidx.room.*
import com.k2703120.healthmate.data.model.HealthRecord
import kotlinx.coroutines.flow.Flow

// Handles database operations for health records
@Dao
interface HealthRecordDao {

    // Insert a new health record
    @Insert
    suspend fun insertRecord(record: HealthRecord)

    // Retrieve all health records
    @Query("SELECT * FROM health_records ORDER BY date DESC, id DESC")
    fun getAllRecords(): Flow<List<HealthRecord>>

    // Update an existing health record
    @Update
    suspend fun updateRecord(record: HealthRecord)

    // Delete an existing health record
    @Delete
    suspend fun deleteRecord(record: HealthRecord)

    // Filter health records by date
    @Query("SELECT * FROM health_records WHERE date = :date ORDER BY id DESC")
    fun getRecordsByDate(date: String): Flow<List<HealthRecord>>

    // Retrieve a specific health record
    @Query("SELECT * FROM health_records WHERE id = :id")
    suspend fun getRecordById(id: Int): HealthRecord?
}