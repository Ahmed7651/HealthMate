package com.k2703120.healthmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.k2703120.healthmate.navigation.HealthNavigation
import com.k2703120.healthmate.ui.theme.HealthMateTheme
import com.k2703120.healthmate.viewmodel.HealthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            HealthMateTheme {

                val healthViewModel: HealthViewModel =
                    viewModel()

                HealthNavigation(
                    viewModel = healthViewModel
                )
            }
        }
    }
}