package cat.covidcontact.tracker.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cat.covidcontact.data.repositories.user.UserException
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.Device
import cat.covidcontact.model.UserDevice
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.notify
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.usecases.getuserdata.GetUserData
import cat.covidcontact.usecases.registerDevice.RegisterDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserData: GetUserData,
    private val registerDevice: RegisterDevice
) : BaseViewModel() {
    private val _userDevice = MutableLiveData<UserDevice>()
    val userDevice: LiveData<UserDevice>
        get() = _userDevice

    private val getUserDataHandler = UseCaseResultHandler<GetUserData.Response>(
        onSuccess = { MainState.UserInfoFound(it.user) },
        onFailure = {
            when (it) {
                is UserException.UserInfoNotFound -> MainState.UserInfoNotFound(it.email)
                else -> ScreenState.Nothing
            }
        }
    )

    private val registerDeviceHandler = UseCaseResultHandler<RegisterDevice.Response>(
        onSuccess = {
            _userDevice.value = it.userDevice
            MainState.DeviceRegistered
        },
        onFailure = { ScreenState.Nothing }
    )

    fun onGetCurrentUser(email: String) {
        viewModelScope.launch {
            executeUseCase(getUserData, getUserDataHandler) {
                GetUserData.Request(email)
            }
        }
    }

    fun onRegisterDevice(user: User, device: Device) {
        viewModelScope.launch {
            executeUseCase(registerDevice, registerDeviceHandler) {
                RegisterDevice.Request(user, device)
            }
        }
    }

    fun onAddContactNetwork(contactNetwork: ContactNetwork) {
        _userDevice.value?.user?.addContactNetwork(contactNetwork)
        _userDevice.notify()
    }
}