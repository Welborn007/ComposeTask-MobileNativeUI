package com.macdevelopers.composetaskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.macdevelopers.composetaskapp.ui.navigation.AppNavGraph
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTaskAppTheme {
                AppNavGraph()
            }
        }
    }
}