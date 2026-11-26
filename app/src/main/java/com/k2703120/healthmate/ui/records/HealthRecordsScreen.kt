package com.k2703120.healthmate.ui.records

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.k2703120.healthmate.data.model.HealthRecord
import com.k2703120.healthmate.ui.theme.*
import com.k2703120.healthmate.viewmodel.HealthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthRecordsScreen(
    viewModel: HealthViewModel,
    onEdit: (HealthRecord) -> Unit
) {
    val records by viewModel.filteredRecords
        .collectAsStateWithLifecycle()

    val selectedDate by viewModel.selectedDate
        .collectAsStateWithLifecycle()

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var recordToDelete by remember {
        mutableStateOf<HealthRecord?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HealthBackground)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Health History",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = HealthPrimary
        )

        Text(
            text = "Review and manage your health records",
            fontSize = 14.sp,
            color = HealthSecondaryText
        )

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = selectedDate.ifEmpty {
                        "Filter by Date"
                    },
                    color = HealthPrimary
                )
            }

            if (selectedDate.isNotEmpty()) {
                TextButton(
                    onClick = {
                        viewModel.clearDateFilter()
                    }
                ) {
                    Text("Clear", color = HealthPrimary)
                }
            }
        }

        Text(
            text = "${records.size} record(s) found",
            fontSize = 14.sp,
            color = HealthSecondaryText
        )

        if (records.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No health records found.",
                    color = HealthSecondaryText
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                items(records, key = { it.id }) { record ->

                    HealthRecordCard(
                        record = record,
                        onEdit = { onEdit(record) },
                        onDelete = {
                            recordToDelete = record
                        }
                    )
                }
            }
        }
    }

    // Date filtering dialog
    if (showDatePicker) {

        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dateFormat.parse(
                selectedDate.ifEmpty {
                    viewModel.getTodayDate()
                }
            )?.time
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.selectDate(
                                dateFormat.format(Date(it))
                            )
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("Apply")
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

    // Delete confirmation dialog
    recordToDelete?.let { record ->

        AlertDialog(
            onDismissRequest = {
                recordToDelete = null
            },
            title = {
                Text("Delete Record")
            },
            text = {
                Text(
                    "Are you sure you want to delete " +
                            "this health record? This action " +
                            "cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteRecord(record)
                        recordToDelete = null
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        recordToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Reusable card for individual health records
@Composable
private fun HealthRecordCard(
    record: HealthRecord,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = record.date,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = HealthPrimary
            )

            HorizontalDivider()

            Text(
                text = "Steps walked: ${record.steps} steps",
                color = StepsColor,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Calories burned: ${record.calories} kcal",
                color = CaloriesColor,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "Water intake: ${record.water} ml",
                color = WaterColor,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Text("Edit", color = HealthPrimary)
                }

                TextButton(onClick = onDelete) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}