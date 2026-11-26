package com.k2703120.healthmate.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.*
import com.k2703120.healthmate.data.model.HealthRecord
import com.k2703120.healthmate.ui.dashboard.DashboardScreen
import com.k2703120.healthmate.ui.records.HealthRecordsScreen
import com.k2703120.healthmate.ui.records.RecordFormScreen
import com.k2703120.healthmate.ui.theme.*
import com.k2703120.healthmate.viewmodel.HealthViewModel

@Composable
fun HealthNavigation(viewModel: HealthViewModel) {

    val navController = rememberNavController()

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute = backStackEntry?.destination?.route

    var selectedRecord by remember {
        mutableStateOf<HealthRecord?>(null)
    }

    Scaffold(
        containerColor = HealthBackground,

        bottomBar = {

            NavigationBar(
                containerColor = HealthSurface
            ) {

                NavigationBarItem(
                    selected = currentRoute == "dashboard",
                    onClick = {
                        navController.navigate("dashboard") {
                            popUpTo("dashboard")
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Text("⌂", color = HealthPrimary)
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = currentRoute == "add",
                    onClick = {
                        selectedRecord = null

                        navController.navigate("add") {
                            popUpTo("dashboard")
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Text("+", color = HealthPrimary)
                    },
                    label = { Text("Add Record") }
                )

                NavigationBarItem(
                    selected = currentRoute == "records" ||
                            currentRoute == "edit",
                    onClick = {
                        navController.navigate("records") {
                            popUpTo("dashboard")
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Text("☰", color = HealthPrimary)
                    },
                    label = { Text("History") }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("dashboard") {
                DashboardScreen(viewModel)
            }

            composable("add") {
                RecordFormScreen(
                    viewModel = viewModel,
                    onSaved = {
                        navController.navigate("records") {
                            popUpTo("dashboard")
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable("records") {
                HealthRecordsScreen(
                    viewModel = viewModel,
                    onEdit = { record ->
                        selectedRecord = record
                        navController.navigate("edit")
                    }
                )
            }

            composable("edit") {
                selectedRecord?.let { record ->

                    RecordFormScreen(
                        viewModel = viewModel,
                        existingRecord = record,
                        onSaved = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}