package com.macdevelopers.composetaskapp.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.composetaskapp.ui.components.AppButton
import com.macdevelopers.composetaskapp.ui.components.AppCard
import com.macdevelopers.composetaskapp.ui.components.AppText
import com.macdevelopers.composetaskapp.ui.theme.BackgroundWhite
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import com.macdevelopers.composetaskapp.ui.theme.Dimens
import com.macdevelopers.composetaskapp.ui.theme.LoginClickable
import com.macdevelopers.composetaskapp.ui.theme.LoginEmailYellow
import com.macdevelopers.composetaskapp.ui.theme.LoginGreetingText
import com.macdevelopers.composetaskapp.ui.theme.LoginPasswordWhite
import com.macdevelopers.composetaskapp.ui.theme.LoginTextSecondary
import androidx.compose.ui.text.font.FontWeight
import com.macdevelopers.composetaskapp.ui.components.AppTextField
import com.macdevelopers.composetaskapp.ui.theme.LoginTextPrimary


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onResetClick: () -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = state.passwordErrorRes?.let { stringResource(it) }


    if (state.loginSuccess) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
            viewModel.onLoginHandled()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(
                it
            )
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->

        LoginScreenBody(
            modifier = Modifier.padding(padding),
            state = state,
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::onLoginClicked,
            onCreateAccountClick = onCreateAccountClick,
            onResetClick = onResetClick
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenBodyPreview() {
    ComposeTaskAppTheme {
        LoginScreenBody(
            state = LoginUiState(
                username = "test@example.com",
                password = "123456",
                usernameErrorRes = null,
                passwordErrorRes = null,
                isLoading = false
            ),
            onUsernameChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onCreateAccountClick = {},
            onResetClick = {}
        )
    }
}



@Composable
fun LoginScreenBody(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onResetClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.ScreenPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens.CardPadding)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(Dimens.TitleSpacing))

                AppText(text = stringResource(R.string.label_login_greetingsText), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = LoginGreetingText)

                Spacer(modifier = Modifier.height(Dimens.TitleSpacing))

                Row {
                    AppText(text = stringResource(R.string.label_login_text1), style = MaterialTheme.typography.bodySmall, color = LoginTextSecondary)
                    Spacer(modifier = Modifier.width(Dimens.SmallSpacing))
                    AppText(text = stringResource(R.string.label_login_text2), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = LoginTextPrimary, onClick = onCreateAccountClick)
                }

                Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

                /* Email TextField */
                AppTextField(
                    value = state.username,
                    onValueChange = onUsernameChange,
                    placeholder = stringResource(R.string.label_login_emailField),
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.usernameErrorRes != null,
                    keyboardType = KeyboardType.Email,
                    icon = Icons.Default.Email,
                    backgroundColor = LoginEmailYellow
                )

                state.usernameErrorRes?.let {
                    Spacer(modifier = Modifier.height(Dimens.SmallSpacing))
                    AppText(text = stringResource(it), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(Dimens.FieldSpacing))

                /* Password TextField */
                AppTextField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    placeholder = stringResource(R.string.label_login_passwordField),
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.passwordErrorRes != null,
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                    backgroundColor = LoginPasswordWhite
                )

                state.passwordErrorRes?.let {
                    Spacer(modifier = Modifier.height(Dimens.SmallSpacing))
                    AppText(text = stringResource(it), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(Dimens.TitleSpacing))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppText(text = stringResource(R.string.label_login_forgotPassword), style = MaterialTheme.typography.bodySmall, color = LoginTextSecondary)
                    AppText(text = stringResource(R.string.label_login_resetPassword), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = LoginClickable, onClick = onResetClick)
                }

                Spacer(modifier = Modifier.height(Dimens.LargeButtonSpacing))

                AppButton(
                    text = stringResource(R.string.label_login_btnText),
                    onClick = onLoginClick,
                    isLoading = state.isLoading
                )
            }
        }
    }
}
