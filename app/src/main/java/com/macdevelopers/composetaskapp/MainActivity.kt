package com.macdevelopers.composetaskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.macdevelopers.shared.domain.usecase.IsUserLoggedInUseCase
import com.macdevelopers.composetaskapp.ui.navigation.AppNavGraph
import com.macdevelopers.composetaskapp.ui.navigation.Screen
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val isUserLoggedIn: IsUserLoggedInUseCase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val loggedIn = isUserLoggedIn()
            val startDestination = if (loggedIn) Screen.Home.route else Screen.Login.route

            setContent {
                ComposeTaskAppTheme {
                    AppNavGraph(startDestination = startDestination)
                }
            }
        }
    }
}