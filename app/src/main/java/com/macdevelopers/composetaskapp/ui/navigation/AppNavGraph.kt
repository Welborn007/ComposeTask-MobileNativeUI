package com.macdevelopers.composetaskapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.macdevelopers.composetaskapp.ui.screens.home.HomeScreen
import com.macdevelopers.composetaskapp.ui.screens.login.LoginScreen
import com.macdevelopers.composetaskapp.ui.screens.signup.SignupScreen
import com.macdevelopers.composetaskapp.ui.screens.vendorProfile.VendorProfileScreen

@Composable
fun AppNavGraph(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onCreateAccountClick = {
                    navController.navigate(Screen.Signup.route){
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onResetClick = {
                    navController.navigate(Screen.Reset.route)
                }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onVendorProfileClick = {
                    navController.navigate(Screen.VendorProfile.route)
                }
            )
        }

        composable(Screen.VendorProfile.route) {
            VendorProfileScreen(
                onBackClick = {
                    navController.navigate(Screen.Home.route){
                        popUpTo(0)
                    }
                },
                onCreateProfileClick = {},
                onEditProfileClick = {}
            )
        }
    }
}
