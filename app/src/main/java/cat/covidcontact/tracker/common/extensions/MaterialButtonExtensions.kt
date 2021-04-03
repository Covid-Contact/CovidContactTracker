package cat.covidcontact.tracker.common.extensions

import com.google.android.material.button.MaterialButton

fun MaterialButton.enableIf(condition: () -> Boolean) {
    isEnabled = condition()
}
