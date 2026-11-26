package com.k2703120.healthmate.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HealthMetricCard(
    title: String,
    value: Int,
    unit: String,
    goal: Int,
    metricColor: Color,
    backgroundColor: Color
) {
    val progress = if (goal > 0) {
        (value.toFloat() / goal).coerceIn(0f, 1f)
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = metricColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$value $unit",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = metricColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = metricColor,
                trackColor = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Daily goal: $goal $unit",
                fontSize = 12.sp,
                color = metricColor
            )
        }
    }
}