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
import androidx.compose.runtime.getValue
import com.macdevelopers.composetaskapp.R
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.macdevelopers.composetaskapp.ui.components.AppButton
import com.macdevelopers.composetaskapp.ui.components.AppCard
import com.macdevelopers.composetaskapp.ui.components.AppText
import com.macdevelopers.composetaskapp.ui.theme.BackgroundWhite
import com.macdevelopers.composetaskapp.ui.theme.ComposeTaskAppTheme
import com.macdevelopers.composetaskapp.ui.theme.Dimens
import com.macdevelopers.composetaskapp.ui.theme.LoginClickable
import com.macdevelopers.composetaskapp.ui.theme.LoginEmailYellow
import com.macdevelopers.composetaskapp.ui.theme.LoginPasswordWhite
import com.macdevelopers.composetaskapp.ui.theme.LoginTextSecondary
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import com.macdevelopers.composetaskapp.ui.components.AppTextField
import com.macdevelopers.composetaskapp.ui.theme.LoginTextPrimary

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    SignupScreenBody(
        name = name,
        onNameChange = { name = it },
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        onSignupClick = {
            isLoading = true
            // TODO: perform signup logic
            // onSignupSuccess()
        },
        onLoginClick = onLoginClick,
        isLoading = isLoading
    )
}

@Composable
fun SignupScreenBody(
    name: String,
    email: String,
    password: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignupClick: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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

                /* ---------------- NAME ---------------- */

                AppTextField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = stringResource(R.string.label_signup_name),
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = LoginPasswordWhite
                )

                Spacer(modifier = Modifier.height(Dimens.FieldSpacing))

                /* ---------------- EMAIL ---------------- */

                AppTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = stringResource(R.string.label_signup_email),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email,
                    icon = Icons.Default.Email,
                    backgroundColor = LoginEmailYellow
                )

                Spacer(modifier = Modifier.height(Dimens.FieldSpacing))

                /* ---------------- PASSWORD ---------------- */

                AppTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = stringResource(R.string.label_signup_password),
                    modifier = Modifier.fillMaxWidth(),
                    isPassword = true,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                    backgroundColor = LoginPasswordWhite
                )

                Spacer(modifier = Modifier.height(Dimens.LargeButtonSpacing))

                /* ---------------- SIGNUP BUTTON ---------------- */

                AppButton(
                    text = stringResource(R.string.label_signup_button),
                    onClick = onSignupClick,
                    isLoading = isLoading
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenBodyPreview() {
    ComposeTaskAppTheme {
        SignupScreenBody(
            name = "",
            email = "",
            password = "",
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onSignupClick = {},
            onLoginClick = {},
            isLoading = false
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
