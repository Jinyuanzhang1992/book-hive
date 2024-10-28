package social.bondoo.bookhive.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import social.bondoo.bookhive.components.CustomTextField
import social.bondoo.bookhive.components.InputFieldProps
import social.bondoo.bookhive.navigation.BookHiveScreens
import social.bondoo.bookhive.utils.isEmailValid
import social.bondoo.bookhive.utils.isPasswordValid
import social.bondoo.bookhive.utils.passwordMatch

@SuppressLint("UnrememberedMutableState")
@Composable
fun RegisterForm(navController: NavHostController, loginScreenViewModel: LoginScreenViewModel) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val confirmPassword = rememberSaveable { mutableStateOf("") }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailValid = remember() {
        mutableStateOf(true)
    }
    val passwordValid = remember() {
        mutableStateOf(true)
    }
    val confirmPasswordValid = remember() {
        mutableStateOf(true)
    }
    val passwordMatch = remember() {
        mutableStateOf(true)
    }
    val allFieldNotEmpty = remember(email.value, password.value, confirmPassword.value) {
        email.value.trim().isNotEmpty() && password.value.trim()
            .isNotEmpty() && confirmPassword.value.trim().isNotEmpty()
    }
    val showError = remember {
        derivedStateOf { !emailValid.value || !passwordValid.value || !confirmPasswordValid.value || !passwordMatch.value }
    }
    val errorMessage by derivedStateOf {
        when {
            !passwordValid.value && !emailValid.value && !confirmPasswordValid.value -> "Invalid Email and Password"
            !emailValid.value -> "Invalid Email"
            !passwordValid.value -> "Invalid Password, Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number and one special character including @\$!%*?&+=#^()-"
            !confirmPasswordValid.value -> "Invalid Confirm Password，Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number and one special character including @\$!%*?&+=#^()-"
            !passwordMatch.value -> "Password does not match"
            else -> ""
        }
    }
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showError.value) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        CustomTextField(
            config = InputFieldProps(
                valueState = email,
                label = "Email",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onAction = KeyboardActions {
                    passwordFocusRequester.requestFocus()
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            modifier = Modifier.focusRequester(passwordFocusRequester),
            config = InputFieldProps(
                valueState = password,
                label = "Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                isPasswordField = true,
                onAction = KeyboardActions {
                    confirmPasswordFocusRequester.requestFocus()
                },
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            modifier = Modifier.focusRequester(confirmPasswordFocusRequester),
            config = InputFieldProps(
                valueState = confirmPassword,
                label = "Confirm Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPasswordField = true,
                onAction = KeyboardActions {
                    if (!valid) return@KeyboardActions
                    keyboardController?.hide()
                },
            )
        )
        Spacer(modifier = Modifier.height(35.dp))
        Button(valid = allFieldNotEmpty, title = "Register") {
            if (!allFieldNotEmpty) return@Button
            when (isEmailValid(email.value)) {
                true -> emailValid.value = true
                false -> emailValid.value = false
            }
            when (isPasswordValid(password.value)) {
                true -> passwordValid.value = true
                false -> passwordValid.value = false
            }
            when (isPasswordValid(confirmPassword.value)) {
                true -> confirmPasswordValid.value = true
                false -> confirmPasswordValid.value = false
            }
            when (passwordMatch(password.value, confirmPassword.value)) {
                true -> passwordMatch.value = true
                false -> passwordMatch.value = false
            }
            if (!showError.value) {
                loginScreenViewModel.createUserWithEmailAndPassword(email.value, password.value) {
                    navController.navigate(route = BookHiveScreens.HomeScreen.name)
                }
            }
        }
    }
}