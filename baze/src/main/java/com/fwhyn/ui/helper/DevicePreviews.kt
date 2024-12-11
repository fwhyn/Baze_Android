/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fwhyn.ui.helper

import androidx.compose.ui.tooling.preview.Preview

/**
 * Multi-preview annotation that represents various device sizes. Add this annotation to a composable
 * to render various devices.
 */
@Preview(showBackground = true, device = "spec:shape=Normal,width=324,height=720,unit=dp,dpi=480", name = "portrait")
@Preview(showBackground = true, device = "spec:shape=Normal,width=720,height=324,unit=dp,dpi=480", name = "landscape")
annotation class DevicePreviews