package com.example.datastoredemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.datastoredemo.data.DataStoreManager
import com.example.datastoredemo.MainScreen
import com.example.datastoredemo.MainViewModel
import com.example.datastoredemo.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Creer le DataStoreManager
        val dataStoreManager = DataStoreManager(this)

        // 2. Creer la Factory pour le ViewModel
        val factory = MainViewModelFactory(dataStoreManager, applicationContext)

        setContent {
            // Observer le mode sombre depuis DataStore
            val isDark by dataStoreManager.darkModeFlow
                .collectAsState(initial = false)

            // Appliquer le theme clair ou sombre selon la preference
            MaterialTheme(
                colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
            ) {
                Surface {
                    val viewModel: MainViewModel = viewModel(factory = factory)
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}