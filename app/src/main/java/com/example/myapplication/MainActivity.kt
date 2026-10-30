package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.navigation.RecipeNavGraph
import com.example.myapplication.ui.theme.RecipeBookTheme
import com.example.myapplication.viewmodel.SettingsViewModel
import com.example.myapplication.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = ViewModelFactory(applicationContext)
            )
            val darkMode by settingsViewModel.darkMode.collectAsStateWithLifecycle()

            RecipeBookTheme(darkTheme = darkMode) {
                RecipeNavGraph()
            }
        }
    }
}
