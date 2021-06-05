/*
 *  Copyright 2021 Albert Pinto i Gil and Santiago Del Rey Juarez
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cat.covidcontact.tracker.common.extensions

import androidx.appcompat.app.AppCompatDelegate

/**
 * Enable the dark theme in the application.
 *
 * @param isEnabled Indicates whether the dark theme should be enabled or not
 */
fun enableDarkTheme(isEnabled: Boolean) {
    val uiMode =
        if (isEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
    AppCompatDelegate.setDefaultNightMode(uiMode)
}
