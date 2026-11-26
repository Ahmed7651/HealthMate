package com.k2703120.healthmate.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.k2703120.healthmate.ui.components.HealthMetricCard
import com.k2703120.healthmate.ui.theme.*
import com.k2703120.healthmate.viewmodel.HealthViewModel

@Composable
fun DashboardScreen(
    viewModel: HealthViewModel
) {
    val records by viewModel.allRecords.collectAsStateWithLifecycle()

    val today = viewModel.getTodayDate()

    // Retrieve today's health records
    val todayRecords = records.filter { it.date == today }

    // Calculate today's activity totals
    val totalSteps = todayRecords.sumOf { it.steps }
    val totalCalories = todayRecords.sumOf { it.calories }
    val totalWater = todayRecords.sumOf { it.water }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HealthBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "HealthMate",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = HealthPrimary
        )

        Text(
            text = "Your personal wellness journey",
            fontSize = 15.sp,
            color = HealthSecondaryText
        )

        HorizontalDivider()

        Text(
            text = "Today's Overview",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = HealthText
        )

        Text(
            text = today,
            fontSize = 14.sp,
            color = HealthSecondaryText
        )

        HealthMetricCard(
            title = "Steps Walked",
            value = totalSteps,
            unit = "steps",
            goal = 10000,
            metricColor = StepsColor,
            backgroundColor = StepsBackground
        )

        HealthMetricCard(
            title = "Calories Burned",
            value = totalCalories,
            unit = "kcal",
            goal = 500,
            metricColor = CaloriesColor,
            backgroundColor = CaloriesBackground
        )

        HealthMetricCard(
            title = "Water Intake",
            value = totalWater,
            unit = "ml",
            goal = 2500,
            metricColor = WaterColor,
            backgroundColor = WaterBackground
        )

        if (todayRecords.isEmpty()) {
            Text(
                text = "No health records added today.",
                color = HealthSecondaryText,
                fontSize = 14.sp
            )
        }
    }
}