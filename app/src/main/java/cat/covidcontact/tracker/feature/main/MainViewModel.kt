package cat.covidcontact.tracker.feature.main

import android.util.Log
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
import cat.covidcontact.usecases.sendread.SendRead
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
    private val getUserData: GetUserData,
    private val registerDevice: RegisterDevice,
    private val sendRead: SendRead
) : BaseViewModel() {
    private val _userDevice = MutableLiveData<UserDevice>()
    val userDevice: LiveData<UserDevice>
        get() = _userDevice

    private val idList: MutableLiveData<MutableSet<String>> = MutableLiveData(mutableSetOf())
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
        onFailure = { ScreenState.Nothing }
    )

    private val sendReadHandler = UseCaseResultHandler<SendRead.Response>(
        onSuccess = { response ->
            //idList.value?.clear()
            if (!response.isEnded) {
                workManager.enqueueUniqueWork(
                    FINISH_INTERACTION_NAME,
                    ExistingWorkPolicy.REPLACE,
                    finishInteraction
                )
            }
            ScreenState.Nothing
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

    fun onLoadBluetoothInfo() {
        loadState(MainState.BluetoothInfo)
    }

    fun onConfigureMessageClient() {
        viewModelScope.launch {
            val isSubscribed = setUpMessageListener()
            if (isSubscribed) {
                setUpWorkManager()
            }
        }
    }

    private suspend fun setUpMessageListener(): Boolean {
        val messageListener = object : MessageListener() {
            override fun onFound(message: Message) {
                val strMessage = String(message.content)
                Log.i("Read", "onFound: $strMessage")

                //idList.value?.add(strMessage)
                viewModelScope.launch {
                    workManager.cancelUniqueWork(FINISH_INTERACTION_NAME)
                    executeUseCase(sendRead, sendReadHandler, isExecutingUseCaseStateLoad = false) {
                        SendRead.Request(
                            currentDeviceId = userDevice.requireValue().device.id,
                            deviceIds = setOf(strMessage),
                            time = System.currentTimeMillis()
                        )
                    }
                }
            }
        }

        val task = messagesClient.subscribe(messageListener)
        task.await()

        task.exception?.printStackTrace()
        return task.isSuccessful
    }

    private fun setUpWorkManager() {
        FinishInteractionWorker.onSend = {
            viewModelScope.launch {
                Log.i("Read", "Send empty interaction")
                onFinishInteraction()
            }
        }

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
            executeUseCase(sendRead, sendReadHandler, isExecutingUseCaseStateLoad = false) {
                SendRead.Request(
                    currentDeviceId = userDevice.requireValue().device.id,
                    deviceIds = emptySet(),
                    time = System.currentTimeMillis()
                )
            }
        }
    }

    fun requireUserDevice(): UserDevice = userDevice.value ?: throw Exception("UserDevice not set")

    companion object {
        private const val PERIOD = 16L
        private const val PUBLISH_WORK_NAME = "publish"
        private const val FINISH_INTERACTION_NAME = "send"
    }
}
