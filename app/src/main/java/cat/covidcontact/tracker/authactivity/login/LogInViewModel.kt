package cat.covidcontact.tracker.authactivity.login

import androidx.lifecycle.ViewModel
import cat.covidcontact.tracker.util.FieldValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val fieldValidator: FieldValidator
) : ViewModel() {

}
