/*
 *  Copyright (C) 2021  Albert Pinto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cat.covidcontact.tracker.feature.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.*
import cat.covidcontact.data.repositories.user.UserException
import cat.covidcontact.model.ContactNetwork
import cat.covidcontact.model.Device
import cat.covidcontact.model.UserDevice
import cat.covidcontact.model.user.User
import cat.covidcontact.tracker.ScreenState
import cat.covidcontact.tracker.common.BaseViewModel
import cat.covidcontact.tracker.common.extensions.notify
import cat.covidcontact.tracker.common.extensions.requireValue
import cat.covidcontact.tracker.common.handlers.UseCaseResultHandler
import cat.covidcontact.tracker.messages.FinishInteractionWorker
import cat.covidcontact.tracker.messages.PublishCodeWorker
import cat.covidcontact.usecases.getuserdata.GetUserData
import cat.covidcontact.usecases.registerDevice.RegisterDevice
import cat.covidcontact.usecases.sendmessagingtoken.SendMessagingToken
import cat.covidcontact.usecases.sendread.SendRead
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import com.google.android.gms.nearby.messages.MessagesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val messagesClient: MessagesClient,
    private val workManager: WorkManager,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val getUserData: GetUserData,
    private val registerDevice: RegisterDevice,
    private val sendRead: SendRead,
    private val sendMessagingToken: SendMessagingToken
) : BaseViewModel() {
    private val _userDevice = MutableLiveData<UserDevice>()
    val userDevice: LiveData<UserDevice>
        get() = _userDevice

    private val isSkipping = MutableLiveData(false)

    private val finishInteraction = OneTimeWorkRequestBuilder<FinishInteractionWorker>().build()

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
            PublishCodeWorker.deviceId = it.userDevice.device.id
            MainState.DeviceRegistered
        },
        onFailure = {
            ScreenState.Nothing
        },
        isCommonEnabled = false
    )

    private val sendReadHandler = UseCaseResultHandler<SendRead.Response>(
        onSuccess = { response ->
            if (!response.isEnded) {
                workManager.enqueueUniqueWork(
                    FINISH_INTERACTION_NAME,
                    ExistingWorkPolicy.REPLACE,
                    finishInteraction
                )
            }
            ScreenState.Nothing
        },
        onFailure = { ScreenState.Nothing },
        isCommonEnabled = false
    )

    private val sendMessagingTokenHandler = UseCaseResultHandler<SendMessagingToken.Response>(
        onSuccess = { MainState.MessagingTokenSent },
        onFailure = { ScreenState.OtherError }
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

    fun onLoadBluetoothInfo() {
        loadState(MainState.BluetoothInfo)
    }

    fun onConfigureMessageClient(context: Context) {
        viewModelScope.launch {
            val isSubscribed = setUpMessageListener(context)
            if (isSubscribed) {
                setUpWorkManager(context)
            }
        }
    }

    private suspend fun setUpMessageListener(context: Context): Boolean {
        val messageListener = object : MessageListener() {
            override fun onFound(message: Message) {
                val strMessage = String(message.content)
                Log.i("Read", "onFound: $strMessage")

                viewModelScope.launch {
                    if (!isSkipping.requireValue()) {
                        workManager.cancelUniqueWork(FINISH_INTERACTION_NAME)
                        val coordinates = if (isLocationPermissionGranted(context)) {
                            getLocationCoordinates()
                        } else {
                            null
                        }

                        executeUseCase(
                            sendRead,
                            sendReadHandler,
                            isExecutingUseCaseStateLoad = false
                        ) {
                            SendRead.Request(
                                currentDeviceId = userDevice.requireValue().device.id,
                                deviceIds = setOf(strMessage),
                                time = System.currentTimeMillis(),
                                lat = coordinates?.first,
                                lon = coordinates?.second
                            )
                        }
                    } else {
                        isSkipping.value = false
                    }
                }
            }
        }

        val task = messagesClient.subscribe(messageListener)
        task.await()

        task.exception?.printStackTrace()
        return task.isSuccessful
    }


    @SuppressLint("MissingPermission")
    private suspend fun getLocationCoordinates(): Pair<Double, Double>? {
        val task = fusedLocationProviderClient.lastLocation
        task.await()

        return if (task.isSuccessful) task.result?.let { location ->
            location.latitude to location.longitude
        } else null
    }

    private fun setUpWorkManager(context: Context) {
        FinishInteractionWorker.onSend = {
            viewModelScope.launch {
                Log.i("Read", "Send empty interaction")
                onFinishInteraction()
            }
        }

        PublishCodeWorker.context = context
        PublishCodeWorker.onCheckBluetoothPermission = ::isBluetoothPermissionGranted

        val publishWork = PeriodicWorkRequestBuilder<PublishCodeWorker>(PERIOD, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            PUBLISH_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            publishWork
        )
    }

    fun onFinishInteraction() {
        viewModelScope.launch {
            if (userDevice.value != null) {
                val coordinates = getLocationCoordinates()

                executeUseCase(sendRead, sendReadHandler, isExecutingUseCaseStateLoad = false) {
                    SendRead.Request(
                        currentDeviceId = userDevice.requireValue().device.id,
                        deviceIds = emptySet(),
                        time = System.currentTimeMillis(),
                        lat = coordinates?.first,
                        lon = coordinates?.second
                    )
                }
            }
        }
    }

    fun onSendMessagingToken() {
        viewModelScope.launch {
            executeUseCase(sendMessagingToken, sendMessagingTokenHandler) {
                SendMessagingToken.Request(requireUserDevice().user.email)
            }
        }
    }

    fun onSetSkip(isSkipEnabled: Boolean) {
        isSkipping.value = isSkipEnabled
    }

    fun requireUserDevice(): UserDevice = userDevice.value ?: throw Exception("UserDevice not set")

    private fun isLocationPermissionGranted(context: Context) =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    private fun isBluetoothPermissionGranted(context: Context) =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) ==
            PackageManager.PERMISSION_GRANTED

    companion object {
        private const val PERIOD = 16L
        private const val PUBLISH_WORK_NAME = "publish"
        private const val FINISH_INTERACTION_NAME = "send"
    }
}
