package social.bondoo.bookhive.screens.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import social.bondoo.bookhive.components.IconWithName
import social.bondoo.bookhive.components.InputFieldProps
import social.bondoo.bookhive.navigation.BookHiveScreens
import social.bondoo.bookhive.screens.layout.Layout
import social.bondoo.bookhive.screens.layout.TopBarType
import social.bondoo.bookhive.utils.isEmailValid
import social.bondoo.bookhive.utils.isPasswordValid

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginScreenViewModel: LoginScreenViewModel
) {
    val loading by loginScreenViewModel.loading.observeAsState(false)
    val title = rememberSaveable {
        mutableStateOf("Login")
    }
    Layout(
        navController = navController,
        showBottomBar = false,
        showTopBar = true,
        title = title.value,
        topBarType = TopBarType.LoginScreen,
    ) {
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LoginScreenContent(
                    navController = navController,
                    title = title,
                    loginScreenViewModel
                )
            }
        }
    }
}

@Composable
fun LoginScreenContent(
    navController: NavHostController,
    title: MutableState<String>,
    loginScreenViewModel: LoginScreenViewModel
) {
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    val reminder1 =
        if (showLoginForm.value) "Don't have an account?" else "Already have an account?"
    val reminder2 = if (showLoginForm.value) "Sign Up" else "Login"
    IconWithName()
    Spacer(modifier = Modifier.height(25.dp));
    when (showLoginForm.value) {
        true -> LoginForm(navController = navController, loginScreenViewModel)
        false -> RegisterForm(navController = navController, loginScreenViewModel)
    }
    Spacer(modifier = Modifier.height(25.dp))
    Reminder(
        reminder1 = reminder1,
        reminder2 = reminder2,
    ) {
        showLoginForm.value = !showLoginForm.value
        title.value = if (showLoginForm.value) "Login" else "Register"
    }
}


@SuppressLint("UnrememberedMutableState")
@Composable
private fun LoginForm(
    navController: NavHostController,
    loginScreenViewModel: LoginScreenViewModel
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val emailValid = remember() {
        mutableStateOf(true)
    }
    val passwordValid = remember() {
        mutableStateOf(true)
    }
    val allFieldNotEmpty = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val loginErrorMessage by loginScreenViewModel.errorMessage.observeAsState("")
    val showError = remember {
        derivedStateOf { !emailValid.value || !passwordValid.value || loginErrorMessage.isNotEmpty() }
    }
    val errorMessage by derivedStateOf {
        when {
            !passwordValid.value && !emailValid.value -> "Invalid Email and Password"
            !emailValid.value -> "Invalid Email"
            !passwordValid.value -> "Invalid Password, Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number and one special character including @\$!%*?&+=#^()-"
            loginErrorMessage.isNotEmpty() -> loginErrorMessage
            else -> ""
        }
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
                    if (!emailValid.value) return@KeyboardActions
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
                imeAction = ImeAction.Done,
                isPasswordField = true,
                onAction = KeyboardActions {
                    if (!passwordValid.value) return@KeyboardActions
                    keyboardController?.hide()
                },
            )
        )
        Spacer(modifier = Modifier.height(35.dp))
        Button(valid = allFieldNotEmpty, title = "Login") {
            if (!allFieldNotEmpty) return@Button
            when (isEmailValid(email.value)) {
                true -> emailValid.value = true
                false -> emailValid.value = false
            }
            when (isPasswordValid(password.value)) {
                true -> passwordValid.value = true
                false -> passwordValid.value = false
            }

            loginScreenViewModel.clearErrorMessage()
            if (!showError.value) {
                loginScreenViewModel.signInWithEmailAndPassword(email.value, password.value) {
                    navController.navigate(route = BookHiveScreens.HomeScreen.name)
                }
            }


            Log.d("login", "LoginForm: Email: ${email.value}, Password: ${password.value}")
            //            navController.navigate(route = BookHiveScreens.HomeScreen.name)
        }
    }
}

