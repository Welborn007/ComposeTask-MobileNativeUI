package com.macdevelopers.composetaskapp.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.macdevelopers.composetaskapp.R
import androidx.hilt.navigation.compose.hiltViewModel
import com.macdevelopers.composetaskapp.ui.theme.PurpleGrey40

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.checkLogin { loggedIn ->
            if (loggedIn) onNavigateToHome()
            else onNavigateToLogin()
        }
    }
    SplashScreenBody()
}

@Composable
fun SplashScreenBody() {
    Box(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ) {
        // 🔼 Logo at top
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
                .size(120.dp),
            contentScale = ContentScale.Fit
        )

        Text(stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = PurpleGrey40,
            modifier = Modifier
                .align(Alignment.Center)
        )

        Column(modifier = Modifier.padding(bottom = 20.dp).align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(stringResource(id = R.string.powered_by),
                style = MaterialTheme.typography.labelSmall)

            Text(stringResource(id = R.string.developer_name),
                style = MaterialTheme.typography.headlineSmall)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreenBody()
}
