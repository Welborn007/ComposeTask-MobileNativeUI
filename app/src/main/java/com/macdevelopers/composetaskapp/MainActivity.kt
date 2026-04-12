package com.macdevelopers.composetaskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.macdevelopers.composetaskapp.domain.usecase.IsUserLoggedInUseCase
import com.macdevelopers.composetaskapp.ui.navigation.AppNavGraph
import com.macdevelopers.composetaskapp.ui.navigation.Screen
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var isUserLoggedIn: IsUserLoggedInUseCase

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