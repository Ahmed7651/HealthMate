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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List


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

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HealthPrimary,
                        selectedTextColor = HealthPrimary,
                        indicatorColor = StepsBackground,
                        unselectedIconColor = HealthSecondaryText,
                        unselectedTextColor = HealthSecondaryText
                    ),
                    selected = currentRoute == "dashboard",
                    onClick = {
                        navController.navigate("dashboard") {
                            popUpTo("dashboard")
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    },
                    label = { Text("Home") }


                )

                NavigationBarItem(

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HealthPrimary,
                        selectedTextColor = HealthPrimary,
                        indicatorColor = StepsBackground,
                        unselectedIconColor = HealthSecondaryText,
                        unselectedTextColor = HealthSecondaryText
                    ),
                    selected = currentRoute == "add",
                    onClick = {
                        selectedRecord = null

                        navController.navigate("add") {
                            popUpTo("dashboard")
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Record")
                    },
                    label = { Text("Add Record") }
                )

                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = HealthPrimary,
                        selectedTextColor = HealthPrimary,
                        indicatorColor = StepsBackground,
                        unselectedIconColor = HealthSecondaryText,
                        unselectedTextColor = HealthSecondaryText
                    ),
                    selected = currentRoute == "records" ||
                            currentRoute == "edit",
                    onClick = {
                        navController.navigate("records") {
                            popUpTo("dashboard")
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.List, contentDescription = "History")
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