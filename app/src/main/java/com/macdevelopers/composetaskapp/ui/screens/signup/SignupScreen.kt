package com.macdevelopers.composetaskapp.ui.screens.signup

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.composetaskapp.ui.components.AppButton
import com.macdevelopers.composetaskapp.ui.components.AppCard
import com.macdevelopers.composetaskapp.ui.components.AppText
import com.macdevelopers.composetaskapp.ui.components.AppTextField
import com.macdevelopers.composetaskapp.ui.components.NetworkErrorBanner
import com.macdevelopers.composetaskapp.ui.theme.BackgroundWhite
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import com.macdevelopers.composetaskapp.ui.theme.Dimens
import com.macdevelopers.composetaskapp.ui.theme.LoginClickable
import com.macdevelopers.composetaskapp.ui.theme.LoginEmailYellow
import com.macdevelopers.composetaskapp.ui.theme.LoginPasswordWhite
import com.macdevelopers.composetaskapp.ui.theme.LoginTextSecondary
import com.macdevelopers.composetaskapp.ui.theme.LoginTextPrimary

@Composable
fun SignupScreen(
    viewModel: SignupViewModel = hiltViewModel(),
    onSignupSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.signupSuccess) {
        LaunchedEffect(Unit) {
            onSignupSuccess()
            viewModel.onSignupHandled()
        }
    }

    SignupScreenBody(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSignupClick = viewModel::onSignupClicked,
        onLoginClick = onLoginClick
    )
}

@Composable
fun SignupScreenBody(
    state: SignupUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignupClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var showNetworkError by remember { mutableStateOf(state.networkError) }

    LaunchedEffect(state.networkError) {
        showNetworkError = state.networkError
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NetworkErrorBanner(
            message = stringResource(R.string.error_no_internet),
            isVisible = showNetworkError,
            modifier = Modifier.fillMaxWidth()
        )

        Box(
            modifier = Modifier
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

                    AppText(text = stringResource(R.string.label_signup_title), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(Dimens.TitleSpacing))

                    Row {
                        AppText(text = stringResource(R.string.label_signup_have_account), style = MaterialTheme.typography.bodySmall, color = LoginTextSecondary)
                        Spacer(modifier = Modifier.width(Dimens.SmallSpacing))
                        AppText(text = stringResource(R.string.label_signup_login), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = LoginTextPrimary, onClick = onLoginClick)
                    }

                    Spacer(modifier = Modifier.height(Dimens.SectionSpacing))

                    /* Name TextField */
                    AppTextField(
                        value = state.name,
                        onValueChange = onNameChange,
                        placeholder = stringResource(R.string.label_signup_name),
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.nameErrorRes != null,
                        backgroundColor = LoginPasswordWhite
                    )

                    state.nameErrorRes?.let {
                        Spacer(modifier = Modifier.height(Dimens.SmallSpacing))
                        AppText(text = stringResource(it), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(modifier = Modifier.height(Dimens.FieldSpacing))

                    /* Email TextField */
                    AppTextField(
                        value = state.email,
                        onValueChange = onEmailChange,
                        placeholder = stringResource(R.string.label_signup_email),
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.emailErrorRes != null,
                        keyboardType = KeyboardType.Email,
                        icon = Icons.Default.Email,
                        backgroundColor = LoginEmailYellow
                    )

                    state.emailErrorRes?.let {
                        Spacer(modifier = Modifier.height(Dimens.SmallSpacing))
                        AppText(text = stringResource(it), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(modifier = Modifier.height(Dimens.FieldSpacing))

                    /* Password TextField */
                    AppTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        placeholder = stringResource(R.string.label_signup_password),
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

                    Spacer(modifier = Modifier.height(Dimens.LargeButtonSpacing))

                    AppButton(
                        text = stringResource(R.string.label_signup_button),
                        onClick = onSignupClick,
                        isLoading = state.isLoading
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenBodyPreview() {
    ComposeTaskAppTheme {
        SignupScreenBody(
            state = SignupUiState(
                name = "",
                email = "",
                password = ""
            ),
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onSignupClick = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
    ComposeTaskAppTheme {
        SignupScreen(
            onSignupSuccess = {},
            onLoginClick = {}
        )
    }
}
