//package com.fwhyn.appsample.ui.feature.auth
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Key
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material3.Button
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusDirection
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavOptions
//import androidx.navigation.compose.composable
//import androidx.navigation.navOptions
//import com.fwhyn.atmsehat.ui.config.AppTheme
//import com.fwhyn.atmsehat.ui.config.MyValue
//import com.fwhyn.atmsehat.ui.feature.home.HomeScreen.Companion.navigateToHomeScreen
//import com.fwhyn.atmsehat.ui.helper.DevicePreviews
//import com.fwhyn.atmsehat.ui.main.AppState
//import com.fwhyn.atmsehat.ui.main.AppState.Companion.rememberAppState
//import com.fwhyn.atmsehat.ui.main.MainUiState
//import com.fwhyn.atmsehat.ui.main.removeFromBackStack
//
//class LoginScreen {
//    companion object {
//        const val LOGIN_ROUTE = "LOGIN_ROUTE"
//
//        fun NavGraphBuilder.addLoginScreen(
//            appState: AppState,
//            mainUiState: MainUiState,
//        ) {
//            composable(LOGIN_ROUTE) {
//                // we place the our Home composable function here
//                // in a production application home will probably take in some parameters
//                Route(
//                    appState = appState,
//                    mainUiState = mainUiState,
//                )
//            }
//        }
//
//        fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
//            this.navigate(LOGIN_ROUTE, navOptions)
//        }
//
//        @Composable
//        fun Route(
//            modifier: Modifier = Modifier,
//            appState: AppState,
//            mainUiState: MainUiState,
//            vm: LoginViewModel = hiltViewModel(),
//        ) {
//            vm.run {
//                Create(
//                    modifier = modifier,
//                    appState = appState,
//                    mainUiState = mainUiState,
//                    loginVmInterface = vm,
//                    loginUiData = loginUiData,
//                    loginUiState = loginUiState
//                )
//            }
//        }
//
//        @Composable
//        fun Create(
//            modifier: Modifier = Modifier,
//            appState: AppState,
//            mainUiState: MainUiState,
//            loginVmInterface: LoginVmInterface,
//            loginUiData: LoginUiData,
//            loginUiState: LoginUiState,
//        ) {
//            val context = LocalContext.current
//            val scope = rememberCoroutineScope()
//            val snackbarHostState = remember { SnackbarHostState() }
//
//            mainUiState.run {
//                when (val state = loginUiState.state) {
//                    is LoginUiState.State.OnNotification -> state.invokeOnce {
//                        showNotification(state.message)
//                    }
//
//                    LoginUiState.State.Idle -> setIdle()
//
//                    LoginUiState.State.Loading -> showLoading()
//
//                    is LoginUiState.State.LoggedIn -> {
//                        state.invokeOnce {
//                            setIdle()
//                            appState.navController.navigateToHomeScreen(navOptions { removeFromBackStack(LOGIN_ROUTE) })
//                        }
//                    }
//
//                    is LoginUiState.State.OnFinish -> {} // Do nothing
//                }
//            }
//
//            loginUiData.run {
//                MainView(
//                    modifier = modifier,
//                    emailValue = email,
//                    onEmailValueChange = loginVmInterface::onEmailValueChange,
//                    passwordValue = pwd,
//                    onPasswordValueChange = loginVmInterface::onPasswordValueChange,
//                    stationIdValue = id,
//                    onStationIdValueChange = loginVmInterface::onStationIdValueChange,
//                    rememberMe = remember,
//                    onCheckRememberMe = loginVmInterface::onCheckRememberMe,
//                    isFieldNotEmpty = isNotEmpty,
//                    onLogin = loginVmInterface::onLogin,
//                )
//            }
//        }
//
//        @Composable
//        fun MainView(
//            modifier: Modifier = Modifier,
//            emailValue: String,
//            onEmailValueChange: (String) -> Unit,
//            passwordValue: String,
//            onPasswordValueChange: (String) -> Unit,
//            stationIdValue: String,
//            onStationIdValueChange: (String) -> Unit,
//            rememberMe: Boolean,
//            onCheckRememberMe: () -> Unit,
//            isFieldNotEmpty: Boolean,
//            onLogin: () -> Unit,
//        ) {
//            Column(
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = modifier
//                    .fillMaxSize()
//                    .padding(MyValue.defaultPadding)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                val commonFieldModifier = Modifier.fillMaxWidth()
//
//                Title(
//                    modifier = commonFieldModifier,
//                )
//                Spacer(modifier = Modifier.height(20.dp))
//                EmailField(
//                    modifier = commonFieldModifier,
//                    value = emailValue,
//                    onValueChange = onEmailValueChange,
//                )
//                PasswordField(
//                    modifier = commonFieldModifier,
//                    value = passwordValue,
//                    onValueChange = onPasswordValueChange,
//                )
//                StationIdField(
//                    modifier = commonFieldModifier,
//                    value = stationIdValue,
//                    onValueChange = onStationIdValueChange,
//                    onKeyboardDone = { onLogin() },
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                Column(
//                    horizontalAlignment = Alignment.End,
//                    modifier = commonFieldModifier
//                ) {
//                    LabeledCheckbox(
//                        label = "Remember Me",
//                        onCheckChanged = onCheckRememberMe,
//                        isChecked = rememberMe
//                    )
//                }
//                Spacer(modifier = Modifier.height(20.dp))
//                Button(
//                    onClick = { onLogin() },
//                    enabled = isFieldNotEmpty,
//                    shape = RoundedCornerShape(5.dp),
//                    modifier = commonFieldModifier
//                ) {
//                    Text("Login")
//                }
//            }
//        }
//
//        @Composable
//        fun LabeledCheckbox(
//            label: String,
//            onCheckChanged: () -> Unit,
//            isChecked: Boolean,
//        ) {
//
//            Row(
//                Modifier
//                    .clickable(
//                        onClick = onCheckChanged
//                    )
//                    .padding(4.dp)
//            ) {
//                Checkbox(checked = isChecked, onCheckedChange = null)
//                Spacer(Modifier.size(6.dp))
//                Text(label)
//            }
//        }
//
//        @Composable
//        fun Title(
//            modifier: Modifier = Modifier,
//        ) {
//            Column(
//                modifier = modifier,
//            ) {
//                Text(
//                    text = "Welcome to ATM Sehat",
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//                Text(
//                    text = "Log in",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//
//        @OptIn(ExperimentalMaterial3Api::class)
//        @Composable
//        fun EmailField(
//            modifier: Modifier = Modifier,
//            value: String = "",
//            onValueChange: (String) -> Unit,
//            label: String = "Nurse Email",
//            placeholder: String = "Enter your Email",
//        ) {
//
//            val focusManager = LocalFocusManager.current
//            val leadingIcon = @Composable {
//                Icon(
//                    Icons.Default.Person,
//                    contentDescription = "",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
//
//            TextField(
//                modifier = modifier,
//                value = value,
//                onValueChange = onValueChange,
//                leadingIcon = leadingIcon,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Email,
//                    imeAction = ImeAction.Next
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                ),
//                label = { Text(label) },
//                placeholder = { Text(placeholder) },
//                singleLine = true,
//            )
//        }
//
//        @OptIn(ExperimentalMaterial3Api::class)
//        @Composable
//        fun PasswordField(
//            modifier: Modifier = Modifier,
//            value: String,
//            onValueChange: (String) -> Unit,
//            label: String = "Password",
//            placeholder: String = "Enter your Password",
//        ) {
//
//            var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
//            val focusManager = LocalFocusManager.current
//            val leadingIcon = @Composable {
//                Icon(
//                    Icons.Default.Key,
//                    contentDescription = "",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
//            val trailingIcon = @Composable {
//                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
//                    Icon(
//                        if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
//                        contentDescription = "",
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                }
//            }
//
//            TextField(
//                modifier = modifier,
//                value = value,
//                onValueChange = onValueChange,
//                leadingIcon = leadingIcon,
//                trailingIcon = trailingIcon,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Password,
//                    imeAction = ImeAction.Next
//                ),
//                keyboardActions = KeyboardActions(
//                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
//                ),
//                label = { Text(label) },
//                placeholder = { Text(placeholder) },
//                singleLine = true,
//                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
//            )
//        }
//
//        @OptIn(ExperimentalMaterial3Api::class)
//        @Composable
//        fun StationIdField(
//            modifier: Modifier = Modifier,
//            value: String = "",
//            onValueChange: (String) -> Unit,
//            onKeyboardDone: () -> Unit,
//            label: String = "Station Id",
//            placeholder: String = "Enter your Id",
//        ) {
//
//            val leadingIcon = @Composable {
//                Icon(
//                    Icons.Default.Person,
//                    contentDescription = "",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
//
//            TextField(
//                modifier = modifier,
//                value = value,
//                onValueChange = onValueChange,
//                leadingIcon = leadingIcon,
//                keyboardOptions = KeyboardOptions(
//                    imeAction = ImeAction.Done,
//                    keyboardType = KeyboardType.Password
//                ),
//                keyboardActions = KeyboardActions(
//                    onDone = { onKeyboardDone() }
//                ),
//                label = { Text(label) },
//                placeholder = { Text(placeholder) },
//                singleLine = true,
//            )
//        }
//    }
//
//    @DevicePreviews
//    @Composable
//    fun LoginScreenPreview() {
//        AppTheme {
//            Create(
//                appState = rememberAppState(),
//                mainUiState = MainUiState(),
//                loginVmInterface = object : LoginVmInterface() {},
//                loginUiData = LoginUiData(),
//                loginUiState = LoginUiState(),
//            )
//        }
//    }
//}