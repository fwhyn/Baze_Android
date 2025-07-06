package com.fwhyn.app.deandro.feature.presentation.home

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.fwhyn.app.deandro.common.ui.config.MyTheme
import com.fwhyn.app.deandro.common.ui.config.defaultPadding
import com.fwhyn.app.deandro.common.ui.dialog.BazeAlertDialog
import com.fwhyn.app.deandro.common.ui.dialog.TryBottomDialog
import com.fwhyn.app.deandro.feature.presentation.login.navigateToLoginScreen
import com.fwhyn.lib.baze.common.helper.extension.removeFromBackStack
import com.fwhyn.lib.baze.compose.helper.ActivityState
import com.fwhyn.lib.baze.compose.helper.DevicePreviews
import com.fwhyn.lib.baze.compose.helper.rememberActivityState
import java.io.File

const val HOME_ROUTE = "homeRoute"

fun NavGraphBuilder.addHomeScreen(
    activityState: ActivityState,
) {
    composable(HOME_ROUTE) {
        HomeScreenRoute(
            activityState = activityState,
        )
    }
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    this.navigate(HOME_ROUTE, navOptions)
}

@Composable
private fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    activityState: ActivityState,
    vm: HomeViewModel = hiltViewModel(),
) {
    vm.run {
        HomeScreen(
            modifier = modifier,
            activityState = activityState,
            vm = vm,
            uiData = uiData,
            uiState = uiState,
        )
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    activityState: ActivityState,
    vm: HomeVmInterface,
    uiData: HomeUiData,
    uiState: HomeUiState,
) {
//    uiData.imagePicker = ImageStorageUtil.registerPickerLauncher(
//        HomeViewModel.MAX_PHOTOS_LIMIT,
//        vmInterface::onPhotoSelected
//    )

    when (val state = uiState.state) {
        is HomeUiState.State.CallPhotoEdit -> state.invokeOnce {
//                activityState.navController.navigateToPhotoEditScreen(key = state.key)
        }

        is HomeUiState.State.LoggedOut -> state.invokeOnce {
            activityState.navigation.navigateToLoginScreen(navOptions {
                removeFromBackStack(
                    HOME_ROUTE
                )
            })
        }

        HomeUiState.State.Idle -> {} // Do nothing
    }

    MainHomeView(
        modifier = modifier,
        onLogout = vm::onLogout,
        onAddPhoto = vm::onAddPhoto,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeView(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
    onAddPhoto: (Activity) -> Unit,
) {

    val activity: Activity = LocalActivity.current!!

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(defaultPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        LogoutButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onLogout,
        )

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Greeting(name = "HomeScreen")
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Working in Progress"
            )

            Spacer(modifier = Modifier.height(10.dp))

            val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(options[0]) }

            val modifierMaxWidth = Modifier.fillMaxWidth()

            ExposedDropdownMenuBox(
                modifier = modifierMaxWidth,
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                OutlinedTextField(
                    modifier = modifierMaxWidth.menuAnchor(MenuAnchorType.PrimaryEditable, true),
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                DropdownMenu(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .exposedDropdownSize(),
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        TextButton(
                            modifier = modifierMaxWidth,
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            },
                        ) {
                            Text(text = selectionOption)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1.0f))
        }

        var isShowingDialog by rememberSaveable { mutableStateOf(false) }

        AddPhotoButton(
            modifier = Modifier.align(Alignment.BottomStart),
            onAddPhoto = {
                onAddPhoto(activity)
            },
//            onAddPhoto = {
//                isShowingDialog = true
//            },
        )

        if (isShowingDialog) {
            TryBottomDialog {
                isShowingDialog = false
            }

            BazeAlertDialog(
                message = "test aja",
                onConfirmation = {

                },
                onDismissRequest = {
                    isShowingDialog = false
                }
            )
        }
    }
}

@Composable
fun LogoutButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.size(40.dp),
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(1.dp)
    ) {
        // Inner content including an icon and a text label
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.AutoMirrored.Filled.Logout,
            contentDescription = "Logout",
        )
    }
}

@Composable
fun PhotoGrid(
    modifier: Modifier,
    photos: List<File>,
    onRemove: ((photo: File) -> Unit)? = null,
) {
    Row(modifier) {
        repeat(photos.size) { index ->
            val file = photos.getOrNull(index)

            if (file == null) {
                Box(Modifier.weight(1f))
            } else {
                Box(
                    contentAlignment = Alignment.TopEnd,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = file,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    if (onRemove != null) {
                        FilledTonalIconButton(onClick = { onRemove(file) }) {
                            Icon(Icons.Filled.Close, null)
                        }
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
        }
    }
}

@Composable
fun AddPhotoButton(
    modifier: Modifier,
    onAddPhoto: () -> Unit,
) {
//            val pickImage = rememberLauncherForActivityResult(
//                ActivityResultContracts.PickMultipleVisualMedia(MAX_PHOTOS_LIMIT),
//                { }
//            )

    Button(
        modifier = modifier.size(40.dp),
        onClick = onAddPhoto,
//                onClick = { pickImage.getMedia(ActivityResultContracts.PickVisualMedia.ImageOnly) },
//                onClick = {
//                    scope?.launch {
//                        pickImage.launch(
//                            PickVisualMediaRequest(
//                                ActivityResultContracts.PickVisualMedia.ImageOnly
//                            )
//                        )
//                    }
//                },
        shape = CircleShape,
        contentPadding = PaddingValues(1.dp)
    ) {
        // Inner content including an icon and a text label
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
        )
    }
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name",
        modifier = modifier
    )
}

@DevicePreviews
@Composable
fun HomeScreenPreview() {
    MyTheme {
        HomeScreen(
            activityState = rememberActivityState(),
            vm = object : HomeVmInterface() {},
            uiData = HomeUiData(),
            uiState = HomeUiState()
        )
    }
}