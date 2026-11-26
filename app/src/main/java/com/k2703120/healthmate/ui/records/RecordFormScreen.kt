package com.k2703120.healthmate.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.k2703120.healthmate.data.model.HealthRecord
import com.k2703120.healthmate.ui.theme.*
import com.k2703120.healthmate.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordFormScreen(
    viewModel: HealthViewModel,
    existingRecord: HealthRecord? = null,
    onSaved: () -> Unit
) {
    var date by remember(existingRecord) {
        mutableStateOf(existingRecord?.date ?: viewModel.getTodayDate())
    }

    var steps by remember(existingRecord) {
        mutableStateOf(existingRecord?.steps?.toString() ?: "")
    }

    var calories by remember(existingRecord) {
        mutableStateOf(existingRecord?.calories?.toString() ?: "")
    }

    var water by remember(existingRecord) {
        mutableStateOf(existingRecord?.water?.toString() ?: "")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val isEditing = existingRecord != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HealthBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = if (isEditing) "Edit Record" else "Add Record",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = HealthPrimary
        )

        Text(
            text = "Manage your daily health activities",
            color = HealthSecondaryText,
            fontSize = 15.sp
        )

        HorizontalDivider()

        Text(
            text = "Activity Date",
            fontWeight = FontWeight.Medium,
            color = HealthText
        )

        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(date, color = HealthPrimary)
        }

        OutlinedTextField(
            value = steps,
            onValueChange = { steps = it },
            label = { Text("Steps Walked") },
            suffix = { Text("steps") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories Burned") },
            suffix = { Text("kcal") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = water,
            onValueChange = { water = it },
            label = { Text("Water Intake") },
            suffix = { Text("ml") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = {

                val stepsValue = steps.toIntOrNull()
                val caloriesValue = calories.toIntOrNull()
                val waterValue = water.toIntOrNull()

                if (
                    stepsValue == null ||
                    caloriesValue == null ||
                    waterValue == null
                ) {
                    errorMessage =
                        "Please enter valid numbers in all fields."

                } else if (
                    stepsValue < 0 ||
                    caloriesValue < 0 ||
                    waterValue < 0
                ) {
                    errorMessage =
                        "Health measurements cannot be negative."

                } else {

                    val record = HealthRecord(
                        id = existingRecord?.id ?: 0,
                        date = date,
                        steps = stepsValue,
                        calories = caloriesValue,
                        water = waterValue
                    )

                    if (isEditing) {
                        viewModel.updateRecord(record)
                    } else {
                        viewModel.insertRecord(record)
                    }

                    errorMessage = ""
                    onSaved()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HealthPrimary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (isEditing) {
                    "Update Record"
                } else {
                    "Save Record"
                },
                fontSize = 16.sp
            )
        }
    }

    if (showDatePicker) {

        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis =
                dateFormat.parse(date)?.time
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            date = dateFormat.format(Date(it))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}