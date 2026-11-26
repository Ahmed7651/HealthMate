package com.k2703120.healthmate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a daily health record in the SQLite database
@Entity(tableName = "health_records")
data class HealthRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String,
    val steps: Int,
    val calories: Int,
    val water: Int
)